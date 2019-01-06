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
  private Map <String,Double> licitacoes = new HashMap<String,Double>(); // licitacoes em espera
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
    this.licitacoes = new HashMap<String,Double>();
    this.nomeServidor = "Comon";
    this.nReservaNormal = 4;
    this.nReservaLicitacao = 4;
    this.nReservadosNormal = 0;
    this.nReservadosLicitacao = 0;
    this.preco = 1;
  }
  
  public Informacao(Map<Integer,Servidor> reservas, Map<String,Double> licitacoes, String nomeServidor, Integer nReservaNormal, Integer nReservaLicitacao, Integer nReservadosNormal, Integer nReservadosLicitacao, Double preco){
    for(Map.Entry<Integer,Servidor> r : reservas.entrySet())
      this.reservas.put(r.getKey(),r.getValue());
     for(Map.Entry<String,Double> l : licitacoes.entrySet())
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
    Map <String,Double> licitacoes = i.getLicitacoes();
    for(Map.Entry<Integer,Servidor> r : reservas.entrySet())
      this.reservas.put(r.getKey(),r.getValue());
    for(Map.Entry<String,Double> l : licitacoes.entrySet())
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

  public Map<String,Double> getLicitacoes(){
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
  
  public int reservaLicitacao(Integer idCliente,Double d,int n,String email){
    l.lock();
    int i;
    try{
      if(this.nReservaLicitacao > 0){ // se existirem  servidores para licitação
        this.nReservaLicitacao--;
        this.nReservadosLicitacao++;
        this.reservas.put(n,new Servidor(2,2,idCliente));
        return n;
      }
      else{ // põe em lista de espera
        this.licitacoes.put(email,d);
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
        this.nReservadosNormal++;
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
        System.out.println("Tipo : " +r.getValue().getTipo() + "; \nReservado ou licitado? : " + r.getValue().getTReserva());
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

  public int mudaDono(String nomeServidor,int numero, int id, Map<String,Cliente> clientes){
    l.lock();
    Cliente c1 = new Cliente();
    Cliente c2 = new Cliente();
    try{
      Servidor r = reservas.get(numero);
      String e1 = null; // cliente que perde server
      String e2 = null; // cliente que reserva server
      for(Map.Entry<String,Cliente> c : clientes.entrySet()){
        if(c.getValue().getId() == r.getIdCliente()) e1 = c.getValue().getEmail();
        if(c.getValue().getId() == id) e2 = c.getValue().getEmail();
        if (e1 != null && e2 != null) break;
      } 
      c1 = clientes.get(e1);
      c2 = clientes.get(e2);
      if(c1.getId() < c2.getId()){
        c1.l.lock();
        c2.l.lock();
      } else{
        c2.l.lock();
        c1.l.lock();
      }
      clientes.get(e1).growDivida(numero); // aumenta divida do cliente
      clientes.get(e1).removeReserva(numero); // remove a reserva da lista de reservas do cliente
      // adiciona a reserva ao novo cliente
      clientes.get(e2).addReserva(numero,new Reserva(nomeServidor,this.preco));
      r.setIdCliente(id);
      r.setTReserva(1);
      return 1;

    } finally{
      this.nReservadosNormal++;
      this.nReservadosLicitacao--;
      c1.l.unlock();
      c2.l.unlock();
      l.unlock();
    }
  }

  public void removeReserva(int i, Map<String,Cliente> clientes){
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
          String e = null;
          int id = -1;
          for(Map.Entry<String,Double> licitacao : this.licitacoes.entrySet()){
            if(licitacao.getValue() > l){
              l = licitacao.getValue();
              e = licitacao.getKey();
            }
          }
          this.licitacoes.remove(e);
          id = clientes.get(e).getId();
          // atualiza ocupante
          this.reservas.get(i).setTReserva(2);
          this.reservas.get(i).setIdCliente(id);
          //atualiza reserva no cliente
          c = clientes.get(e);
          c.l.lock();
          c.addReserva(i,new Reserva(this.nomeServidor,l));
        } else{
          if(i2 == 1){ // estava reservado normalmente
            this.nReservadosNormal--;
            this.nReservaLicitacao++;

          }else{ // estava reservado com licitacao
            this.nReservaLicitacao++;
            this.nReservadosLicitacao--;
          }
          this.reservas.remove(i);
        }
      }
    } finally{
      c.l.unlock();
      l.unlock();
    }
  }
}
