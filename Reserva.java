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


class Reserva { // tempo e preço
  private double preco;
  private long tempo;

  public Reserva(){
    this.preco = 0;
    this.tempo = 0;
  }
  public Reserva(double d){
    this.preco = d;
    this.tempo = System.currentTimeMillis();
  }
  public Reserva(Reserva r){
    this.preco = r.getPreco();
    this.tempo = r.getTReserva();
  }

  public double getPreco(){
    return this.preco;
  }
  public long getTReserva(){
    long t = System.currentTimeMillis();
    return (t - this.tempo) / ( 3600000);
  }
  public Reserva clone(){
    return new Reserva(this);
  }
}
