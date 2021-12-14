package vendingmachine.controller;

import vendingmachine.model.Coin;
import vendingmachine.model.CoinBox;
import vendingmachine.model.Machine;
import vendingmachine.model.ProductTable;
import vendingmachine.validator.ConsoleValidator;
import vendingmachine.validator.ProductValidator;
import vendingmachine.view.InputData;
import vendingmachine.view.MainView;
import vendingmachine.view.OutputData;

public class MachineCore {
    private final Machine machine = new Machine();
    private final MainView view = new MainView();
    private final ConsoleValidator consoleValidator = new ConsoleValidator();
    private final ProductValidator productValidator = new ProductValidator();

    public void startMachine() {
        prepareMachin();
        runMachine();
    }

    private void runMachine() {
        readInputMoneyFromUser();
        while (machine.isStillAvailableToBuy()) {
            String productName = readProductToBuyFromUser();
            machine.sellProduct(productName);
        }
        printInputMoneyLeft();
        printChangeReturned();
    }

    private void prepareMachin() {
        readMachineAssetFromUser();
        printCoinBoxStatus();
        readProductTableFromUser();
    }

    private void readMachineAssetFromUser() {
        while (true) {
            try {
                String input = view.askQuestion(InputData.ASK_BUDGET);
                machine.setInitialAsset(consoleValidator.checkNumeric(input));
                break;
            } catch (IllegalArgumentException exception) {
                view.printError();
            }
        }
        view.printBlankLine();
    }

    private void readProductTableFromUser() {
        while (true) {
            try {
                String input = view.askQuestion(InputData.ASK_PRODUCT_TO_BUY);
                ProductTable productTable = splitProductInfo(input);
                machine.setProductTable(productTable);
                break;
            } catch (IllegalArgumentException exception) {
                view.printError();
            }
        }
        view.printBlankLine();
    }

    private void printCoinBoxStatus() {
        view.printOutput(OutputData.MACHINES_COINS);
        CoinBox coinBox = machine.getCoinBox();
        for (Coin coin : Coin.values()) {
            view.printCoinStatus(coin.getAmount(), coinBox.getCoinCount(coin));
        }
        view.printBlankLine();
    }

    private void printInputMoneyLeft() {
        view.printInputMoneyStatus(machine.getInputMoney());
    }

    private void printChangeReturned() {
        CoinBox change = machine.getAvailableChange();
        view.printOutput(OutputData.CHANGE);

        for (Coin coin : Coin.values()) {
            if (change.getCoinCount(coin) > 0) {
                view.printCoinStatus(coin.getAmount(), change.getCoinCount(coin));
            }
        }
    }

    private ProductTable splitProductInfo(String userInput) {
        return productValidator.checkStringOfProductTable(userInput);
    }

    private void readInputMoneyFromUser() {
        while (true) {
            try {
                String input = view.askQuestion(InputData.ASK_INPUT_MONEY);
                machine.setInputMoney(consoleValidator.checkNumeric(input));
                break;
            } catch (IllegalArgumentException exception) {
                view.printError();
            }
        }
        view.printBlankLine();
    }

    private String readProductToBuyFromUser() {
        printInputMoneyLeft();
        String input;
        while (true) {
            try {
                input = view.askQuestion(InputData.ASK_PRODUCT_TO_BUY);
                productValidator.checkName(input);
                break;
            } catch (IllegalArgumentException exception) {
                view.printError();
            }
        }
        view.printBlankLine();
        return input;
    }
}
