package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import com.jmc.mazebank.Models.Transaction;
import com.jmc.mazebank.Views.AccountType;
import com.jmc.mazebank.Views.BankType;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReplenishController implements Initializable {
    public Text from_text;
    public Text amount_text;
    public ChoiceBox bank_selector;
    public TextField amount;
    public Button trans_btn;
    public Text acc_from_text;
    public TextField acc_from;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bank_selector.setItems(FXCollections.observableArrayList(BankType.MAZEBANK, BankType.TIMEBANK, BankType.EASYBANK, BankType.FPI));
        trans_btn.setOnAction(event -> onTrans());
    }

    private void onTrans() {
        String receiver = Model.getInstance().getClient().pAddressProperty().getValue();
        double amount_h = Double.parseDouble(amount.getText());
        String bank_h = bank_selector.getValue().toString();
        String acc_from_h = acc_from.getText();

        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
        try {
            if (resultSet.isBeforeFirst()){
                Model.getInstance().getDatabaseDriver().updateBalance(receiver, amount_h, "ADD");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        Model.getInstance().getDatabaseDriver().newTransaction(acc_from_h, receiver, amount_h, "Пополнение из " + bank_h);

        amount.setText("");
        acc_from.setText("");
        Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(receiver));
        Model.getInstance().getAllTransactions().add(new Transaction(acc_from_h, receiver, amount_h, LocalDate.now(), "Пополнение из " + bank_h));
    }
}
