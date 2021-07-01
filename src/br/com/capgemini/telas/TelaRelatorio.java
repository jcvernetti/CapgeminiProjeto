package br.com.capgemini.telas;

import java.sql.*;
import br.com.capgemini.dal.ModuloConexao;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.text.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author jcver
 */
public class TelaRelatorio extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    //Lista de variáveis para o relatório
    String nomeCliente, nomeAnuncio, dataInicio, dataTermino, valorDiario;
    Double ncv, ncc, nnvc, nvao, nmcs, nMax;
    int ndias;

    /**
     * Creates new form TelaRelatorio
     */
    public TelaRelatorio() {
        initComponents();
        conexao = ModuloConexao.connector();
        preencher_tabelaClientes();
        carregar_criterios();

    }

    /*método para popular o combobox com o nome dos clientes
    private void preencher_combobox() {
        String sql = "select nomecli from tbclientes";
        try {
            pst = conexao.prepareStatement(sql);
            
            rs = pst.executeQuery();
           while (rs.next()) {
            comboClientes.addItem(rs.getString("nomecli"));
           }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }*/
    //método para carregar critérios
    private void carregar_criterios() {
        String sql = "select * from tbcriterios";
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            //passa os valores dos critérios para as variáveis
            //
            if (rs.next()) {
                ncv = Double.parseDouble(rs.getString(2));
                ncc = Double.parseDouble(rs.getString(3));
                nnvc = Double.parseDouble(rs.getString(4));
                nvao = rs.getDouble(5);
                nmcs = rs.getDouble(6);
                nMax = ncv * ncc * nnvc * nvao * nmcs;
            }
            //

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //método para preencher tabela clientes
    private void preencher_tabelaClientes() {

        //idcli as Id,nomecli as Nome,endcli as Endereço, fonecli as Fone, emailcli as Email
        String sql = "select idcli,nomecli as Nome from tbclientes";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o "?":
            //Atenção ao "%" que continua a concatenação da string sql

            //pst.setString(1, txtCliPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //método para pesquisar clientes pelo nome com filtro
    private void pesquisar_cliente() {

        String sql = "select idcli,nomecli as Nome from tbclientes where nomecli like ?";

        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o "?":
            //Atenção ao "%" que continua a concatenação da string sql

            pst.setString(1, txtCliPesquisar.getText() + "%");

            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //método para preencher tabela Anúncios
    private void preencher_tabelaAnuncios() {

        //Setar recebe a linha selecionada e s recebe o idcli do cliente
        int setar = tblClientes.getSelectedRow();
        String s = tblClientes.getModel().getValueAt(setar, 0).toString();
        nomeCliente = tblClientes.getModel().getValueAt(setar, 1).toString();

        String sql = "select * from tbAnuncios where idcli= " + s;
        try {
            pst = conexao.prepareStatement(sql);
            rs = pst.executeQuery();
            //a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblAnuncios.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void DataDif(){
    String s1,s2;
        java.util.Date d1,d2;
        d1=null;d2=null;
        DateFormat df = new SimpleDateFormat ("dd/MM/yyyy");
        df.setLenient(false);
        s1=dataInicio;
        s2=dataTermino;
        //log(Level.SEVERE, null, ex)   DifDates.class.getName()
        try {  
            d1 = df.parse(s1);
            d2 = df.parse (s2);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, ex);;
        }
        long dt = (d2.getTime() - d1.getTime()) + 3600000; // 1 hora para compensar horário de verão
        ndias = (int)(dt / 86400000L);
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btnImprimir = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        txtCliPesquisar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblAnuncios = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setPreferredSize(new java.awt.Dimension(800, 500));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnImprimir.setText("Imprimir relatório");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });

        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnImprimir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFechar)
                .addGap(42, 42, 42))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnImprimir)
                    .addComponent(btnFechar))
                .addContainerGap())
        );

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setPreferredSize(new java.awt.Dimension(192, 391));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Procurar cliente cadastrado"));
        jPanel4.setPreferredSize(new java.awt.Dimension(345, 52));

        txtCliPesquisar.setToolTipText("Digite a primeira/s letra/s do nome");
        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        tblClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Título 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientes.setToolTipText("Clique para selecionar cliente desejado");
        tblClientes.setFocusable(false);
        tblClientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblClientes);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/capgemini/icones/pesquisar.png"))); // NOI18N
        jLabel5.setToolTipText("Pesquisar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addGap(16, 16, 16))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de anúncios do cliente selecionado"));

        tblAnuncios = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int colIndex){
                return false;
            }
        };
        tblAnuncios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAnuncios.setFocusable(false);
        tblAnuncios.getTableHeader().setReorderingAllowed(false);
        tblAnuncios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAnunciosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblAnuncios);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 369, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 369, Short.MAX_VALUE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(18, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // Imprimir relatório
        JOptionPane.showMessageDialog(null, "Em construção");
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        // Habilita item de menu
        TelaPrincipal.MenuRelatorio.setEnabled(true);
    }//GEN-LAST:event_formInternalFrameClosing

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // Fecha a tela de relatório
        this.dispose();
        TelaPrincipal.MenuRelatorio.setEnabled(true);
    }//GEN-LAST:event_btnFecharActionPerformed

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // Esse evento é do tipo "enquanto for pesquisando...":
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // chama método para preencher tabela Anúncios, clicando com o mouse na tabela Clientes
        //preencher_tabelaAnuncios();
        preencher_tabelaAnuncios();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void tblAnunciosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAnunciosMouseClicked
        double ntempo, ndia, ntotal, visumax, cliqmax, compartimax;

        //Setar recebe a linha selecionada e s recebe o idcli do cliente
        int setar = tblAnuncios.getSelectedRow();
        //nomeCliente,nomeAnuncio, dataInicio, dataTermino, valorDiario;
        nomeAnuncio = tblAnuncios.getModel().getValueAt(setar, 1).toString();
        dataInicio = tblAnuncios.getModel().getValueAt(setar, 2).toString();
        dataTermino = tblAnuncios.getModel().getValueAt(setar, 3).toString();
        valorDiario = tblAnuncios.getModel().getValueAt(setar, 4).toString();
        DataDif();//calcula a diferença em dias das datas
        ntempo = ndias;
        ndia = Double.parseDouble(valorDiario);
        ntotal = ntempo * ndia;
        visumax = ntotal * nMax;
        cliqmax = ntotal * nvao * ncv;
        compartimax = ntotal * nvao * ncv * ncc * nmcs;
        //        amount = Double.parseDouble(jTextField2.getText()) * nMax;// 86.4
        String s = "Relatório de alcance de anúncio online \r\n"
                + "\r\n"
                + "Nome do cliente:  " + nomeCliente + " \r\n"
                 + "\r\n"
                + "Nome do anúncio:  " + nomeAnuncio + " \r\n"
                 + "\r\n"
                + "Data de início:  " + dataInicio + " \r\n"
                + "Data de término:  " + dataTermino + " \r\n"
                + "Intervalo de tempo (em dias): " + String.format("%,.0f", ntempo) + " \r\n"
                 + "\r\n"
                + "Quantidade máxima de visualizações: " + String.format("%,.0f", visumax) + " \r\n"
                + "Quantidade máxima de cliques: " + String.format("%,.0f", cliqmax) + " \r\n"
                + "Quantidade máxima de compartilhamentos: " + String.format("%,.0f", compartimax) + " \r\n"
                + "\r\n"
                + "Valor total investido (R$): " + String.format("%,.2f", ntotal) + " \r\n";       
        jTextArea1.setText(s);
    }//GEN-LAST:event_tblAnunciosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFechar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTable tblAnuncios;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCliPesquisar;
    // End of variables declaration//GEN-END:variables
}
