/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.books.controller;

import poly.books.util.XDialog;

/**
 *
 * @author HuyNguyen
 */
public interface LoginController {
     void open();

    void login();

    default void exit() {
        if (XDialog.confirm("Bạn muốn kết thúc?")) {
            System.exit(0);

        }
    }
}
