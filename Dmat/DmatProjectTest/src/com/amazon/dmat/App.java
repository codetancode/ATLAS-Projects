package com.amazon.dmat;

public class App {
    public static void main( String[] args ){
        
     	System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println( "Welcome to DMAT Account Manager Application" );
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
     	
     	Menu menu = new Menu();
        menu.showMenu();
    }
}
