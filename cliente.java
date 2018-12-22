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


public class Cliente{
  private int id;
  private String password;
  public ReentrantLock l = new ReentrantLock();
  private Map<Integer,Reserva> reservas = new HashMap<Integer,Reserva>();
  private double divida = 0; 

  public Cliente(){
    this.id = -1;
    this.password = "pass";
    this.reservas = new HashMap<Integer,Reserva>();
    this.divida = 0;
  }

  public Cliente(int id,String password, HashMap<Integer,Reserva> reservas, double divida){
    this.id = id;
    this.password = password;
    this.reservas = new HashMap<Integer,Reserva>();
    for(Map.Entry<Integer,Reserva> r : reservas.entrySet()){
      this.reservas.put(r.getKey(),r.getValue());
    }
    this.divida = divida;
  }

  public Cliente(Cliente c){
    this.id = c.getId();
    this.password = c.getPass();
    this.reservas = new HashMap<Integer,Reserva>();
    this.reservas = c.getReservas();
    this.divida = c.getDivida();
  }

  public int getId(){
    return this.id;
  } 
  public String getPass(){
    return this.password;
  }
  public Map<Integer,Reserva> getReservas(){
    Map<Integer,Reserva> r = new HashMap<>();
    for(Map.Entry<Integer,Reserva> n : this.reservas.entrySet())
      r.put(n.getKey(),n.getValue());
    return r;
  }
  public double getDivida(){
    return this.divida;
  }

  public void setId(int id){
    this.id = id;
  }
  public void setPass(String pass){
    this.password = pass;
  }
  public void addReserva(int nReserva,Reserva r){
    this.reservas.put(nReserva,r);
  }
  public void setDivida(double divida){
    this.divida = divida;
  }

  public String cliente2String(){
    StringBuilder s = new StringBuilder();
    s.append("Id: " + this.id);
    s.append("\nPassword : " + this.password);
    for(Map.Entry<Integer,Reserva> r : this.reservas.entrySet()){
      s.append("\n nº da reserva : " + r.getKey() + " --> { Preço : " + r.getValue().getPreco() + ", Tempo da Reserva : " + r.getValue().getTReserva() + "}");
    }
    s.append("\nDivida : " + this.divida);
    return s.toString();
  }

  public Cliente clone(Cliente c){
    return new Cliente(c);
  }

  public void growDivida(double i){
    this.l.lock();
    try{
      // uso assim porque o get não funciona direito (dá erro)
      for(Map.Entry<Integer,Reserva> r : reservas.entrySet())
        if(r.getKey() == i){
          this.divida += r.getValue().getDivida();
      }
    } finally{
      this.l.unlock();
    }
  }

  public void removeReserva(int i){
    this.reservas.remove(i);
  }
}