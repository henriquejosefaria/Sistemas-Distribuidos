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
  private ReentrantLock l = new ReentrantLock();
  private String nomeServidor;      // nome do servidor
  private int nReservaNormal;       // nº de reservados normal
  private int nReservaLicitacao;    // nº de reservados por licitação
  private int nReservadosNormal;    //nº de servidores para reserva
  private int nReservadosLicitacao; //nº de servidores para licitação
  private double preco;             // preco reserva do tipo de servidor
  private int nReserva;             // indice de prox. reserva

  public Informacao(){
    this.reservas = new HashMap<Integer,Servidor>();
    this.licitacoes = new HashMap<Integer,Double>();
    this.nomeServidor = "Comon";
    this.nReservaNormal = 15;
    this.nReservaLicitacao = 15;
    this.nReservadosNormal = 0;
    this.nReservadosLicitacao = 0;
    this.preco = 1;
    this.nReserva = 0;
  }
  
  public Informacao(Map<Integer,Servidor> reservas, Map<Integer,Double> licitacoes, String nomeServidor, Integer nReservaNormal, Integer nReservaLicitacao, Integer nReservadosNormal, Integer nReservadosLicitacao, Double preco,Integer nReserva){
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
    this.nReserva = nReserva;
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
    this.nReserva = i.getnReserva();
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
  
  public int getnReserva(){
    l.lock();
    try{
      return this.nReserva;
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
  
  public void setnReserva(Integer n){
    l.lock();
    try{
      this.nReserva = n;
    } finally{
      l.unlock();
    }
  }

  public int reservaLicitacao(Integer id,Double d){
    l.lock();
    try{

      if(this.nReservaLicitacao > 0){ // se existirem  servidores para licitação
        System.out.println("Pode reservar");
        this.nReservaLicitacao--;
        this.nReservadosLicitacao++;
        this.reservas.put(this.nReserva,new Servidor(2,2,id));
        //modificar licitador


        this.nReserva++;
        return (this.nReserva--);
      }
      else{ // põe em lista de espera
        this.licitacoes.put(id,d);
        return -1;
      } 

    } finally{
      l.unlock();
    }
  }

  public int reservaNormal(Integer id){

    l.lock();
    try{

      if(this.nReservaNormal > 0){
        this.nReservaNormal--;
        this.nReservadosNormal++;
        this.reservas.put(this.nReserva,new Servidor(1,1,id));

        this.nReserva++;
        return (this.nReserva--);
      } else if(this.nReservaLicitacao > 0){
        this.nReservaLicitacao--;
        this.nReservadosLicitacao++;
        this.reservas.put(this.nReserva,new Servidor(2,1,id));
        this.nReserva++;
        return (this.nReserva--);
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
    try{

      Servidor r = reservas.get(numero);
      clientes.get(r.getIdCliente()).growDivida(numero); // aumenta divida do cliente
      clientes.get(r.getIdCliente()).removeReserva(numero);
      clientes.get(id).addReserva(numero,new Reserva(this.preco));
      r.setIdCliente(id);
      r.setTReserva(1);
      return 1;

    } finally{
      l.unlock();
    }
  }

  public void removeReserva(int i, Map<Integer,Cliente> clientes){
    l.lock();
    try{

      int i1 = this.reservas.get(i).getTipo();
      int i2 = this.reservas.get(i).getTReserva(); 
      if(i1 == 1){ // tipo -> reservaNormal
        this.nReservaNormal++;
        this.nReservadosNormal--;
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
          // atualiza ocupante
          this.reservas.get(i).setTReserva(2);
          this.reservas.get(i).setIdCliente(id);
          //atualiza reserva no cliente
          clientes.get(id).addReserva(i,new Reserva(l));
        } else{
          this.nReservaLicitacao++;
          this.nReservadosLicitacao--;
        }
      }
    } finally{
      l.unlock();
    }
  }
}
