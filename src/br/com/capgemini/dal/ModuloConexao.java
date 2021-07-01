/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.capgemini.dal;

import java.sql.*;

/**
 *
 * @author jcver
 */
public class ModuloConexao {
    //método para conexão com o banco de dados
    public static Connection connector() {
        Connection conexao = null;
        //a linha abaixo chama o driver importado para bibliotecas
        String driver = "com.mysql.cj.jdbc.Driver";
        //Infos referentes ao bd
        String url = "jdbc:mysql://localhost:3306/dbanuncios";
        String user = "root";
        String password = "";
        //Estabelecendo conexão com o bd, protegendo contra
        //queda da rede, queima da porta de um sweet
        //Digita try  e depois ctrl + tecla de espaço
        //o método connector() sem void requer um return
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            return null;
        }
    }
}
