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


class Reserva { // nomeServer, tempo e preço
  private String nome = null;
  private double preco = 0;
  private double tempo = 0;

  public Reserva(){
    this.nome = null;
    this.preco = 0;
    this.tempo = 0;
  }
  public Reserva(String nome, double d){
    this.nome = nome;
    this.preco = d;
    this.tempo = System.currentTimeMillis() / 3600000;
  }
  public Reserva(Reserva r){
    this.nome = r.getNome();
    this.preco = r.getPreco();
    this.tempo = r.getTReserva();
  }

  public String getNome(){
    return this.nome;
  }
  public double getPreco(){
    return this.preco;
  }
  public double getTempo(){
    return this.tempo;
  }
  public double getTReserva(){
    long t = System.currentTimeMillis() / 3600000;
    return (double)(t - this.tempo);
  }
  public Reserva clone(){
    return new Reserva(this);
  }
  public String reserva2String(){
    StringBuilder s = new StringBuilder();
    s.append("\nPreço: " + this.preco);
    s.append("\nTempo: " + this.getTempo());
    return s.toString();
  }
  public double getDivida(){
    double i = ((System.currentTimeMillis() / 3600000) - this.tempo);
    if( i <= 0) i = 1;
    return (this.preco * i);
  }
}
