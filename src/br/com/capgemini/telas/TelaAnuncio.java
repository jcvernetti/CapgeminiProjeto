/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.capgemini.telas;

import java.sql.*;
import br.com.capgemini.dal.ModuloConexao;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//a linha abaixo importa recursos da biblioteca rs2xml.jar
import net.proteanit.sql.DbUtils;

/**
 *
 * @author jcver
 */
public class TelaAnuncio extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    String nomeCliente;
    String dataInicio, dataTermino;
    int ndias;

    /**
     * Creates new form TelaAnuncio
     */
    public TelaAnuncio() {
        initComponents();
        btnAlterar.setEnabled(false);
        conexao = ModuloConexao.connector();
        preencher_tabelaClientes();
    }

    //método para adicionar cliente
    private void adicionar() {
        String sql = "insert into tbanuncios (nomeanu,datainicio,datatermino,investedia,idcli) values(?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtAnuncio.getText());
            pst.setString(2, txtDataInicio.getText());
            pst.setString(3, txtDataTermino.getText());
            pst.setString(4, txtInvesteDia.getText());
            pst.setString(5, txtCliId.getText());

            //Validação dos campos obrigatórios
            if ((txtAnuncio.getText().isEmpty()) || (txtDataInicio.getText().isEmpty()) || (txtDataTermino.getText().isEmpty()) || (txtInvesteDia.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                //a linha abaixo atualiza o tbclientes com os dados do formulário
                //a estrutura abaixo serve para confirmar a inserção de dados
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Anúncio adicionado com sucesso");
                    //as linhas abaixo limpam os campos
                    limpar();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //método para pesquisar clientes pelo nome com filtro
    private void preencher_tabelaClientes() {
        //String sql = "select * from tbclientes where nomecli like ?";
        String sql = "select idcli as Id,nomecli as Nome,endcli as Endereço, fonecli as Fone, emailcli as Email from tbclientes";
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

        String sql = "select idcli as Id,nomecli as Nome,endcli as Endereço, fonecli as Fone, emailcli as Email from tbclientes where nomecli like ?";

        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o "?":
            //Atenção ao "%" que continua a concatenação da string sql

            pst.setString(1, txtCliPesquisar.getText() + "%");

            rs = pst.executeQuery();
            //System.out.println(rs);
            //a linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela
            tblClientes.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //método para inserir nos campos edit uma linha clicada da tabela Clientes 
    public void setar_campos_tbClientes() {
        int setar = tblClientes.getSelectedRow();
        txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtAnuncio.setText(null);
        txtDataInicio.setText(null);
        txtDataTermino.setText(null);
        txtInvesteDia.setText(null);
        btnAdicionar.setEnabled(true);
        btnAlterar.setEnabled(false);
    }

    //método para inserir nos campos edit uma linha clicada da tabela Anúncios 
    public void setar_campos_tbAnuncios() {
        int setar = tblAnuncios.getSelectedRow();
        //txtCliId.setText(tblClientes.getModel().getValueAt(setar, 0).toString());
        //txtCliNome.setText(tblClientes.getModel().getValueAt(setar, 1).toString());
        txtAnuncio.setText(tblAnuncios.getModel().getValueAt(setar, 1).toString());
        txtDataInicio.setText(tblAnuncios.getModel().getValueAt(setar, 2).toString());
        txtDataTermino.setText(tblAnuncios.getModel().getValueAt(setar, 3).toString());
        txtInvesteDia.setText(tblAnuncios.getModel().getValueAt(setar, 4).toString());
        btnAlterar.setEnabled(true);
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
   
    //método para alterar dados já registrados
    private void alterar() {//nomeanu,datainicio,datatermino,investedia,idcli
        String sql = "update tbanuncios set nomeanu=?,datainicio=?, datatermino=?,investedia=?,idcli=? where idanu=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, txtAnuncio.getText());
            pst.setString(2, txtDataInicio.getText());
            pst.setString(3, txtDataTermino.getText());
            pst.setString(4, txtInvesteDia.getText());
            pst.setString(5, txtCliId.getText());
            int setar = tblAnuncios.getSelectedRow();
            pst.setString(6, tblAnuncios.getModel().getValueAt(setar, 0).toString());
            //Validação dos campos obrigatórios
            if ((txtAnuncio.getText().isEmpty()) || (txtDataInicio.getText().isEmpty()) || (txtDataTermino.getText().isEmpty()) || (txtInvesteDia.getText().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios");
            } else {

                //a linha abaixo atualiza o tbclientes com os dados do formulário
                //a estrutura abaixo serve para confirmar a inserção de dados
                int adicionado = pst.executeUpdate();
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Anúncio alterado com sucesso");
                    //as linhas abaixo limpam os campos e a tabela Anúncios
                    limpar();
                    btnAdicionar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //remove linha da tabela Anúncios
    private void remover() {
        //antes de deletar, tem que confirmar
        int confirma = JOptionPane.showInternalConfirmDialog(null, "Tem certeza que deseja remover o anúncio selecionado?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirma == JOptionPane.YES_OPTION) {
            String sql = "delete from tbanuncios where idanu=?";
            try {
                pst = conexao.prepareStatement(sql);
                int setar = tblAnuncios.getSelectedRow();
                pst.setString(1, tblAnuncios.getModel().getValueAt(setar, 0).toString());
                int apagado = pst.executeUpdate();
                if (apagado > 0) {
                    JOptionPane.showMessageDialog(null, "Anúncio removido com sucesso");
                    //as linhas abaixo limpam os campos e a tabela Anúncios
                    limpar();
                    btnAdicionar.setEnabled(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //método para limpar os campos do formulário e das tabelas
    private void limpar() {
        txtCliNome.setText(null);
        txtAnuncio.setText(null);
        txtDataInicio.setText(null);
        txtDataTermino.setText(null);
        txtInvesteDia.setText(null);
        tblClientes.clearSelection();
        txtCliId.setText(null);
        ((DefaultTableModel) tblClientes.getModel()).setRowCount(0);
        ((DefaultTableModel) tblAnuncios.getModel()).setRowCount(0);
    }

    //método para calcular a diferença em dias entre duas datas
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        txtCliPesquisar = new javax.swing.JTextField();
        txtCliId = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtCliNome = new javax.swing.JTextField();
        txtAnuncio = new javax.swing.JTextField();
        txtDataInicio = new javax.swing.JFormattedTextField();
        txtDataTermino = new javax.swing.JFormattedTextField();
        txtInvesteDia = new javax.swing.JTextField();
        btnAdicionar = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btbRemover = new javax.swing.JButton();
        btnFechar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblAnuncios = new javax.swing.JTable();

        setClosable(true);
        setTitle("Cadastrar anúncio");
        setPreferredSize(new java.awt.Dimension(900, 500));
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

        tblClientes = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int celIndex){
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
                "Nome", "Fone"
            }
        ));
        tblClientes.setFocusable(false);
        tblClientes.getTableHeader().setReorderingAllowed(false);
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblClientes);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Procurar cliente cadastrado"));
        jPanel1.setPreferredSize(new java.awt.Dimension(345, 52));

        txtCliPesquisar.setToolTipText("Digite a primeira/s letra/s do nome do cliente");
        txtCliPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCliPesquisarKeyReleased(evt);
            }
        });

        txtCliId.setEditable(false);

        jLabel6.setText("Id");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCliPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCliId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 26, Short.MAX_VALUE))
        );

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/capgemini/icones/pesquisar.png"))); // NOI18N
        jLabel5.setToolTipText("Pesquisar");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cadastrar anúncio (* campos obrigatórios)"));

        jLabel3.setText("* Nome do Cliente");

        jLabel4.setText("* Nome do anúncio");

        jLabel7.setText("* Data de início");

        jLabel1.setText("* Data de término");

        jLabel2.setText("* Investimento diário (R$/dia)");

        txtCliNome.setEditable(false);

        try {
            txtDataInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            txtDataTermino.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        txtDataTermino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDataTerminoActionPerformed(evt);
            }
        });

        btnAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/capgemini/icones/create.png"))); // NOI18N
        btnAdicionar.setToolTipText("Adicionar novo anúncio");
        btnAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdicionarActionPerformed(evt);
            }
        });

        btnAlterar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/capgemini/icones/update.png"))); // NOI18N
        btnAlterar.setToolTipText("Alterar e atualizar dados do anúncio selecionado");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });

        btbRemover.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/capgemini/icones/delete.png"))); // NOI18N
        btbRemover.setToolTipText("Remover anúncio selecionado");
        btbRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btbRemoverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(txtInvesteDia, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(22, 22, 22)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(txtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(56, 56, 56)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtDataTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(txtAnuncio, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAdicionar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAlterar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btbRemover)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtCliNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAnuncio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(txtDataTermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtInvesteDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAdicionar)
                    .addComponent(btnAlterar)
                    .addComponent(btbRemover))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnFechar.setText("Fechar");
        btnFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFecharActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Lista de anúncios do cliente selecionado"));

        tblAnuncios = new javax.swing.JTable(){
            public boolean isCellEditable(int rowIndex, int celIndex){
                return false;
            }
        };
        tblAnuncios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "idanu", "nomeanu", "datainicio", "datatermino", "investedia", "idcli"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnFechar)
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 369, Short.MAX_VALUE))))
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(107, 107, 107)
                        .addComponent(btnFechar)))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCliPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCliPesquisarKeyReleased
        // Esse evento é do tipo "enquanto for pesquisando...":
        pesquisar_cliente();
    }//GEN-LAST:event_txtCliPesquisarKeyReleased

    private void txtDataTerminoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDataTerminoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDataTerminoActionPerformed

    private void btnAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdicionarActionPerformed
        // Chama método adicionar:
        dataInicio = txtDataInicio.getText();
        dataTermino = txtDataTermino.getText();
        DataDif();
        if (ndias>0){
            adicionar(); 
        }else {
            JOptionPane.showMessageDialog(null, "A data de término deve ser maior que a data de início");
        }
    }//GEN-LAST:event_btnAdicionarActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        TelaPrincipal.MenuCadastrarAnuncio.setEnabled(true);
    }//GEN-LAST:event_formInternalFrameClosing

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        // Seleciona cliente
        setar_campos_tbClientes();
        preencher_tabelaAnuncios();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFecharActionPerformed
        // Fecha a janela de cadastro de anúncio
        this.dispose();
        TelaPrincipal.MenuCadastrarAnuncio.setEnabled(true);
    }//GEN-LAST:event_btnFecharActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        //chama o método alterar:
        // Chama método adicionar:
        dataInicio = txtDataInicio.getText();
        dataTermino = txtDataTermino.getText();
        DataDif();
        if (ndias>0){
        alterar();
        }else {
            JOptionPane.showMessageDialog(null, "A data de término deve ser maior que a data de início");
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void tblAnunciosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAnunciosMouseClicked
        //Preencher os campos Edtit para alterar ou remover anúncio
        setar_campos_tbAnuncios();
    }//GEN-LAST:event_tblAnunciosMouseClicked

    private void btbRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btbRemoverActionPerformed
        // Chama o método remover:
        int setar = tblAnuncios.getSelectedRow();
        if (setar != -1) {
            remover();
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum anúncio selecionado");
        }
    }//GEN-LAST:event_btbRemoverActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btbRemover;
    private javax.swing.JButton btnAdicionar;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnFechar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tblAnuncios;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtAnuncio;
    private javax.swing.JTextField txtCliId;
    private javax.swing.JTextField txtCliNome;
    private javax.swing.JTextField txtCliPesquisar;
    private javax.swing.JFormattedTextField txtDataInicio;
    private javax.swing.JFormattedTextField txtDataTermino;
    private javax.swing.JTextField txtInvesteDia;
    // End of variables declaration//GEN-END:variables
}
