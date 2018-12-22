import java.util.Scanner;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.lang.Thread;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.*;
import java.io.*;
import java.util.concurrent.locks.ReentrantLock;



class Informacao{ 

  private Map <Integer,Servidor> reservas; // numero de reserva e Servidor reservado (reserva/licitacao)
  private Map <Integer,Double> licitacoes = new HashMap<Integer,Double>(); // licitacoes em espera
  public ReentrantLock l = new ReentrantLock();
  private String nomeServidor;      // nome do servidor
  private int nReservaNormal;       // nº de reservados normal
  private int nReservaLicitacao;    // nº de reservados por licitação
  private int nReservadosNormal;    //nº de servidores para reserva
  private int nReservadosLicitacao; //nº de servidores para licitação
  private double preco;             // preco reserva do tipo de servidor
  //private int nReserva;             // indice de prox. reserva

  public Informacao(){
    this.reservas = new HashMap<Integer,Servidor>();
    this.licitacoes = new HashMap<Integer,Double>();
    this.nomeServidor = "Comon";
    this.nReservaNormal = 15;
    this.nReservaLicitacao = 15;
    this.nReservadosNormal = 0;
    this.nReservadosLicitacao = 0;
    this.preco = 1;
  }
  
  public Informacao(Map<Integer,Servidor> reservas, Map<Integer,Double> licitacoes, String nomeServidor, Integer nReservaNormal, Integer nReservaLicitacao, Integer nReservadosNormal, Integer nReservadosLicitacao, Double preco){
    for(Map.Entry<Integer,Servidor> r : reservas.entrySet())
      this.reservas.put(r.getKey(),r.getValue());
     for(Map.Entry<Integer,Double> l : licitacoes.entrySet())
      this.licitacoes.put(l.getKey(),l.getValue());
    this.nomeServidor = nomeServidor;
    this.nReservaNormal = nReservaNormal;
    this.nReservaLicitacao = nReservaLicitacao;
    this.nReservadosNormal = nReservadosNormal;
    this.nReservadosLicitacao = nReservadosLicitacao;
    this.preco = preco;
  }
  
  public Informacao(Informacao i){
    Map <Integer,Servidor> reservas = i.getReservas();
    Map <Integer,Double> licitacoes = i.getLicitacoes();
    for(Map.Entry<Integer,Servidor> r : reservas.entrySet())
      this.reservas.put(r.getKey(),r.getValue());
    for(Map.Entry<Integer,Double> l : licitacoes.entrySet())
      this.licitacoes.put(l.getKey(),l.getValue());
    this.nomeServidor = i.getNomeServidor();
    this.nReservaNormal = i.getLivresReserva();
    this.nReservaLicitacao = i.getLivresLeilao();
    this.nReservadosNormal = i.getOcupadosReserva();
    this.nReservadosLicitacao = i.getOcupadosLeilao();
    this.preco = i.getPreco();
  }

  public String getNomeServidor(){
    l.lock();
    try{
      return this.nomeServidor;
    } finally{
      l.unlock();
    }
  }
  
  public int getServidoresLivres(){
    l.lock();
    try{
      return (this.getLivresReserva() + this.getLivresLeilao());
    } finally{
      l.unlock();
    }  
  }
  
  public int getLivresReserva(){
    l.lock();
    try{
      return this.nReservaNormal;
    } finally{
      l.unlock();
    }
  }
  
  public int getLivresLeilao(){
    l.lock();
    try{
      return this.nReservaLicitacao;
    } finally{
      l.unlock();
    }
  }
  
  public int getOcupadosReserva(){
    l.lock();
    try{
      return this.nReservadosNormal;
    } finally{
      l.unlock();
    }
  }
  
  public int getOcupadosLeilao(){
    l.lock();
    try{
      return this.nReservadosLicitacao;
    } finally{
      l.unlock();
    }
  }
  
  public double getPreco(){
    l.lock();
    try{
      return this.preco;
    } finally{
      l.unlock();
    }
  }

  public Map<Integer,Servidor> getReservas(){
    l.lock();
    try{
      return this.reservas;
    } finally{
      l.unlock();
    }
  }

  public Map<Integer,Double> getLicitacoes(){
    l.lock();
    try{
      return this.licitacoes;
    } finally{
      l.unlock();
    }
  }

  public void setNomeServidor(String s){
    l.lock();
    try{
      this.nomeServidor = s;
    } finally{
      l.unlock();
    }
  }
  
  public void setLivresReserva(Integer n){
    l.lock();
    try{
      this.nReservaNormal = n;
    } finally{
      l.unlock();
    }
  }
  
  public void setLivresLeilao(Integer n){
    l.lock();
    try{
      this.nReservaLicitacao = n;
    } finally{
      l.unlock();
    }
  }
  
  public void setOcupadosReserva(Integer n){
    l.lock();
    try{
      this.nReservadosNormal = n;
    } finally{
      l.unlock();
    }
  }
  
  public void setOcupadosLeilao(Integer n){
    l.lock();
    try{
      this.nReservadosLicitacao = n;
    } finally{
      l.unlock();
    }
  }
  
  public void setPreco(Double p){
    l.lock();
    try{
      this.preco = p;
    } finally{
      l.unlock();
    }
  }
  
  public int reservaLicitacao(Integer id,Double d,int n){
    l.lock();
    int i;
    try{
      if(this.nReservaLicitacao > 0){ // se existirem  servidores para licitação
        this.nReservaLicitacao--;
        this.nReservadosLicitacao++;
        this.reservas.put(n,new Servidor(2,2,id));
        return n;
      }
      else{ // põe em lista de espera
        this.licitacoes.put(id,d);
        return -1;
      } 

    } finally{
      l.unlock();
    }
  }

  public int reservaNormal(Integer id,int n){
    l.lock();
    int i;
    try{
      if(this.nReservaNormal > 0){
        this.nReservaNormal--;
        this.nReservadosNormal++;
        this.reservas.put(n,new Servidor(1,1,id));
        return n;
      } else if(this.nReservaLicitacao > 0){
        this.nReservaLicitacao--;
        this.nReservadosLicitacao++;
        this.reservas.put(n,new Servidor(2,1,id));
        return n;
      }
      else return -1;

    } finally{
      l.unlock();
    }
  }

  public int isPossible(){
    l.lock();
    try{

      int i;
      for(Map.Entry<Integer,Servidor> r : reservas.entrySet()){
        i = r.getKey();
        // se for servidor leilão e tiver sido leiloado
        if(r.getValue().getTipo() == 2 && r.getValue().getTReserva() == 2){
          return i;
        }
      }
      return -1;

    } finally{
      l.unlock();
    }
  }

  public int mudaDono(int numero, int id, Map<Integer,Cliente> clientes){
    l.lock();
    Cliente c1 = new Cliente();
    Cliente c2 = new Cliente();
    try{

      Servidor r = reservas.get(numero);
      c1 = clientes.get(r.getIdCliente());
      c2 = clientes.get(id);
      if(r.getIdCliente() < id){
        c1.l.lock();
        c2.l.lock();
      } else{
        c2.l.lock();
        c1.l.lock();
      }
      clientes.get(r.getIdCliente()).growDivida(numero); // aumenta divida do cliente
      clientes.get(r.getIdCliente()).removeReserva(numero); // remove a reserva da lista de reservas do cliente
      // adiciona a reserva ao novo cliente
      clientes.get(id).addReserva(numero,new Reserva(this.preco));
      r.setIdCliente(id);
      r.setTReserva(1);
      return 1;

    } finally{
      c1.l.unlock();
      c2.l.unlock();
      l.unlock();
    }
  }

  public void removeReserva(int i, Map<Integer,Cliente> clientes){
    l.lock();
    Cliente c = new Cliente();
    c.l.lock();
    try{
      int i1 = this.reservas.get(i).getTipo();
      int i2 = this.reservas.get(i).getTReserva(); 
      if(i1 == 1){ // tipo -> reservaNormal
        this.nReservaNormal++;
        this.nReservadosNormal--;
        this.reservas.remove(i);
      }else{ // tipo -> reservaLicitação
        if(this.licitacoes.size() > 0){
          double l = 0;
          int id = -1;
          for(Map.Entry<Integer,Double> licitacao : this.licitacoes.entrySet()){
            if(licitacao.getValue() > l){
              l = licitacao.getValue();
              id = licitacao.getKey();
            }
          }
          this.licitacoes.remove(id);
          // atualiza ocupante
          this.reservas.get(i).setTReserva(2);
          this.reservas.get(i).setIdCliente(id);
          //atualiza reserva no cliente
          c = clientes.get(id);
          c.l.lock();
          c.addReserva(i,new Reserva(l));
        } else{
          this.nReservaLicitacao++;
          this.nReservadosLicitacao--;
          this.reservas.remove(i);
        }
      }
    } finally{
      c.l.unlock();
      l.unlock();
    }
  }
}
