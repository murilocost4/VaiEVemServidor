/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
/*aaa*/

import factory.Conector;
import java.sql.*;
import java.util.ArrayList;
import modelDominio.Usuario;
import modelDominio.Admin;
import modelDominio.Condutor;
import modelDominio.Passageiro;

/**
 *
 * @author murilocost4
 */
public class UsuarioDao {
    
    private Connection con;
    
    public UsuarioDao() {
        con = Conector.getConnection();
    }
    
    public Usuario efetuarLogin(Usuario user) {
        // num preparedStatement nós preparamos o SQL para rodar
        PreparedStatement stmt = null;
        // nesta variável guardaremos o usuário
        // selecionado do banco
        Usuario usuarioSelecionado = null;
        
        if (this.con == null) {
            System.out.println("Conexão nula...");
        }
        
        try {
            String sql = "select * from usuario where email = ? and senha = ?";
            // preparando o SQL para substituir os ?s
            stmt = con.prepareStatement(sql);
            // trocando os ? (parametros)
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getSenha());

            // executando o script no banco e guardando o resultado
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                // o .next() verifica se existe um próximo registro
                // descobrindo se o usuário é administrador ou comum
                if (res.getInt("tipo") == 1) {
                    // é administrador
                    usuarioSelecionado = new Admin(res.getInt("user_id"),
                            res.getString("nome"),
                            res.getString("cpf"),
                            res.getString("nascimento"),
                            res.getString("endereco"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("fone"));
                } else if (res.getInt("tipo") == 2) {
                    // é condutor
                    usuarioSelecionado = new Condutor(res.getInt("user_id"),
                            res.getString("nome"),
                            res.getString("cpf"),
                            res.getString("nascimento"),
                            res.getString("endereco"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("fone"));
                } else {
                    usuarioSelecionado = new Passageiro(res.getInt("user_id"),
                            res.getString("nome"),
                            res.getString("cpf"),
                            res.getString("nascimento"),
                            res.getString("endereco"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("fone"));
                }
            }
            // fechar tudo
            res.close();
            stmt.close();
            con.close();
            return usuarioSelecionado;
        } catch (Exception e) {
            e.printStackTrace();
            // se aconteceu erro eu retorno nullo
            return null;
        }
    }
    
    public boolean inserir(Usuario usr) {
        //vai receber o script SQL de INSERT 
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            // desliga o autocommit
            con.setAutoCommit(false);
            // o ? será substituído pelo valor
            String sql = "insert into usuario (nome,cpf,nascimento,endereco,fone,email,senha,tipo)"
                    + " values (?,?,?,?,?,?,?,?) ";
            stmt = con.prepareStatement(sql);
            //substituir os ? do script SQL
            stmt.setString(1, usr.getNomeUsuario());
            stmt.setString(2, usr.getCpf());
            stmt.setString(3, usr.getNascimento());
            stmt.setString(4, usr.getEndereco());
            stmt.setString(5, usr.getFone());
            stmt.setString(6, usr.getEmail());
            stmt.setString(7, usr.getSenha());
            int tipo;
            if (usr instanceof Admin) {
                tipo = 1;
            } else if (usr instanceof Condutor) {
                tipo = 2;
            } else {
                tipo = 3;
            }
            stmt.setInt(8, tipo);

            //executar o SCRIPT SQL
            stmt.execute();
            //efetivar a transação
            con.commit();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
    
    public boolean alterar(Usuario usr) {
        //vai receber o script SQL de INSERT 
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            // desliga o autocommit
            con.setAutoCommit(false);
            // o ? será substituído pelo valor
            String sql = " update usuario set nome = ?,"
                    + " cpf = ?,"
                    + " nascimento = ?,"
                    + " endereco = ?,"
                    + " fone = ?,"
                    + " email = ?,"
                    + " senha = ?,"
                    + "tipo = ? where user_id = ?";
            stmt = con.prepareStatement(sql);
            //substituir os ? do script SQL
            stmt.setString(1, usr.getNomeUsuario());
            stmt.setString(2, usr.getCpf());
            stmt.setString(3, usr.getNascimento());
            stmt.setString(4, usr.getEndereco());
            stmt.setString(5, usr.getFone());
            stmt.setString(6, usr.getEmail());
            stmt.setString(7, usr.getSenha());
            // se for admin guarda 1 senão guarda 0
            int tipo;
            if (usr instanceof Admin) {
                tipo = 1;
            } else if (usr instanceof Condutor) {
                tipo = 2;
            } else {
                tipo = 3;
            }
            //stmt.setInt(5, ((usr instanceof Administrador) ? 1 : 0));
            stmt.setInt(8, tipo);
            stmt.setInt(9, usr.getCodUsuario());

            //executar o SCRIPT SQL
            stmt.execute();
            //efetivar a transação
            con.commit();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
    
    public boolean excluir(Usuario usr) {
        //vai receber o script SQL de INSERT 
        PreparedStatement stmt = null;
        boolean result = false;
        try {
            // desliga o autocommit
            con.setAutoCommit(false);
            // o ? será substituído pelo valor
            String sql = "delete from usuario where user_id = ? ";
            stmt = con.prepareStatement(sql);
            //substituir os ? do script SQL
            stmt.setInt(1, usr.getCodUsuario());

            //executar o SCRIPT SQL
            stmt.execute();
            //efetivar a transação
            con.commit();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                stmt.close();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }
    
    public boolean alteraSenha(int codUsuario, String senha) {
    PreparedStatement stmt = null;
    boolean result = false;

    try {
        // Criar o script SQL para verificar e atualizar o usuário
        String sql = "UPDATE usuario SET senha = ? WHERE user_id = ?";
        stmt = con.prepareStatement(sql);

        // Substituir os parâmetros "?" do script SQL
        stmt.setString(1, senha);
        stmt.setInt(2, codUsuario);

        // Executar o UPDATE e obter o número de linhas afetadas
        int rowsAffected = stmt.executeUpdate();

        // Verificar se alguma linha foi atualizada
        if (rowsAffected > 0) {
            result = true; // Senha alterada com sucesso
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return result;
}

    
    public boolean verificarUsuario(Usuario usr) {
    PreparedStatement stmt = null;
    ResultSet rs = null;
    boolean result = false;

    try {
        // Criar o script SQL para verificar usuário
        String sql = "SELECT * FROM usuario WHERE email = ? AND senha = ?";
        stmt = con.prepareStatement(sql);

        // Substituir os parâmetros "?" do script SQL
        stmt.setString(1, usr.getEmail());
        stmt.setString(2, usr.getSenha());

        // Executar o SELECT e obter os resultados
        rs = stmt.executeQuery();

        // Verificar se a consulta retornou algum registro
        if (rs.next()) {
            result = true; // Usuário encontrado
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return result;
}

    
    public ArrayList<Usuario> getListaUsuarios() {
        Statement stmt = null; // usado para rodar SQL
        ArrayList<Usuario> listausr = new ArrayList<Usuario>();

        try {
            // cria o objeto para rodar o SQL
            stmt = con.createStatement();
            // passando a string SQL que faz o SELECT
            ResultSet res = stmt.executeQuery("select * from usuario");

            // Pebkorrendo o resultado - res
            while (res.next()) {
            Usuario usr;
            int tipo = res.getInt("tipo");

            // Extração de dados comuns
            int id = res.getInt("user_id");
            String nome = res.getString("nome");
            String cpf = res.getString("cpf");
            String nascimento = res.getString("nascimento");
            String endereco = res.getString("endereco");
            String fone = res.getString("fone");
            String email = res.getString("email");
            String senha = res.getString("senha");

            // Construção baseada no tipo
            if (tipo == 1) {
                usr = new Admin(id, nome, cpf, nascimento, endereco, senha, email, fone);
            } else if (tipo == 2) {
                usr = new Condutor(id, nome, cpf, nascimento, endereco, senha, email, fone);
            } else {
                usr = new Passageiro(id, nome, cpf, nascimento, endereco, senha, email, fone);
            }

                // adicionando na lista auxiliar
                listausr.add(usr);
            }
            res.close();// fechando o resultado
            stmt.close();// fechando statment
            con.close(); // fechando conexão com o banco
            return listausr; // retornando a lista de gastomensals
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }
        
    }
    
    public ArrayList<Condutor> getListaCondutor() {
        Statement stmt = null; // usado para rodar SQL
        ArrayList<Condutor> listaCondutor = new ArrayList<Condutor>();

        try {
            // cria o objeto para rodar o SQL
            stmt = con.createStatement();
            // passando a string SQL que faz o SELECT
            ResultSet res = stmt.executeQuery("select * from usuario where tipo = 2");

            // Pebkorrendo o resultado - res
            while (res.next()) {
                Condutor cond;
                cond = new Condutor(res.getInt("user_id"),
                            res.getString("nome"),
                            res.getString("cpf"),
                            res.getString("nascimento"),
                            res.getString("endereco"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("fone"));
                listaCondutor.add(cond);
            }
            res.close();// fechando o resultado
            stmt.close();// fechando statment
            con.close(); // fechando conexão com o banco
            return listaCondutor; // retornando a lista de gastomensals
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }
        
    }
    
    public ArrayList<Passageiro> getListaPassageiro() {
        Statement stmt = null; // usado para rodar SQL
        ArrayList<Passageiro> listaPassageiro = new ArrayList<Passageiro>();

        try {
            // cria o objeto para rodar o SQL
            stmt = con.createStatement();
            // passando a string SQL que faz o SELECT
            ResultSet res = stmt.executeQuery("select * from usuario where tipo = 3");

            // Pebkorrendo o resultado - res
            while (res.next()) {
                Passageiro pas;
                pas = new Passageiro(res.getInt("user_id"),
                            res.getString("nome"),
                            res.getString("cpf"),
                            res.getString("nascimento"),
                            res.getString("endereco"),
                            res.getString("email"),
                            res.getString("senha"),
                            res.getString("fone"));
                listaPassageiro.add(pas);
            }
            res.close();// fechando o resultado
            stmt.close();// fechando statment
            con.close(); // fechando conexão com o banco
            return listaPassageiro; // retornando a lista de gastomensals
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + "-" + e.getMessage());
            return null;
        }
        
    }
    
    
    
}
