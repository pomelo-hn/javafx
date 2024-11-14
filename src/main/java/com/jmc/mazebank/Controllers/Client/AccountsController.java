package com.jmc.mazebank.Controllers.Client;

import com.jmc.mazebank.Models.Model;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_cv_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        trans_to_sv_btn.setOnAction(event -> onSendMoneySv());
        trans_to_cv_btn.setOnAction(event -> onSendMoneyCh());
    }

    private void bindData() {
        sv_acc_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
        ch_acc_bal.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString());
    }

    private void onSendMoneySv() {
        String receiver = Model.getInstance().getClient().pAddressProperty().getValue();
        double amount = Double.parseDouble(amount_to_sv.getText());
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
        try {
            if (resultSet.isBeforeFirst()){
                Model.getInstance().getDatabaseDriver().Sv2Ch(receiver, amount);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        amount_to_sv.setText("");
        Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(receiver));
        Model.getInstance().getClient().checkingAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getChekingAccountBalance(receiver));

    }

    private void onSendMoneyCh() {
        String receiver = Model.getInstance().getClient().pAddressProperty().getValue();
        double amount = Double.parseDouble(amount_to_ch.getText());
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);
        try {
            if (resultSet.isBeforeFirst()){
                Model.getInstance().getDatabaseDriver().Ch2Sv(receiver, amount);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        amount_to_ch.setText("");
        Model.getInstance().getClient().savingsAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(receiver));
        Model.getInstance().getClient().checkingAccountProperty().get().setBalance(Model.getInstance().getDatabaseDriver().getChekingAccountBalance(receiver));
    }
}
