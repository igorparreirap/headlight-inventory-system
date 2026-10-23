package model;

/**
 * Entidade que representa a marca (fabricante) de um farol.
 * Cadastro basico, espelha a tabela marca(id, nome) do banco db_farois.
 * O toString() retorna o nome, facilitando o uso em JComboBox e JTable.
 */
public class Marca {

    private int id;
    private String nome;

    /** Construtor vazio. */
    public Marca() {
    }

    /** Construtor completo. */
    public Marca(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    /** Construtor sem id (uso em inclusoes). */
    public Marca(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
