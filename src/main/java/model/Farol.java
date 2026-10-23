package model;

/**
 * Entidade que representa um farol em estoque (cadastro intermediario).
 * Espelha a tabela farol(id, modelo, ano, tipo, quantidade, preco, id_marca).
 * Mantem um objeto Marca (e nao apenas o id) para transportar o
 * relacionamento N:1 entre as camadas do sistema.
 */
public class Farol {

    private int id;
    private String modelo;
    private int ano;
    private String tipo;
    private int quantidade;
    private double preco;
    private Marca marca;

    /** Construtor vazio. */
    public Farol() {
    }

    /** Construtor completo. */
    public Farol(int id, String modelo, int ano, String tipo,
                 int quantidade, double preco, Marca marca) {
        this.id = id;
        this.modelo = modelo;
        this.ano = ano;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.preco = preco;
        this.marca = marca;
    }

    /** Construtor sem id (uso em inclusoes). */
    public Farol(String modelo, int ano, String tipo,
                 int quantidade, double preco, Marca marca) {
        this.modelo = modelo;
        this.ano = ano;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.preco = preco;
        this.marca = marca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return modelo + " (" + ano + ") - " + tipo;
    }
}
