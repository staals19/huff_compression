import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class letter{
    String letter;
    int value;
    letter left;
    letter right;

    public letter(String letter, int value){
        this.letter = letter;
        this.value = value;
    }
    
    public void set(letter one, letter two){
        if(one.value == two.value){
            this.right = one;
            this.left = two;
        }
        else if(one.value > two.value){
            this.right = one;
            this.left = two;
        }
        else{
            this.right = two;
            this.left = one;
        }
    }
    
}
