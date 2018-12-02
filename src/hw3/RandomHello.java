package hw3;

import java.util.Random;

/* Prints out "Hello World" in a random language */
public class RandomHello {
    
    /**
     * Keeps track of the various hello greetings
     */
    private String[] hellos = {"Hello World", 
            "Hola Mundo", "Bonjour Monde", "Hallo Welt", "Ciao Mondo"};

    /**
     * Prints out the hello greeting
     * @param argv
     */
    public static void main(String[] argv) {
        RandomHello rHello = new RandomHello();
        System.out.println(rHello.getGreeting());
    }
   
    /**
     * @return the random hello greeting
     */
    public String getGreeting() {
        Random rand = new Random();
        return this.hellos[rand.nextInt(5)];
    }
}
