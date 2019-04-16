import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
public class main{
    public static void main(String[] args) throws IOException{
        File f = new File(Paths.get("").toAbsolutePath().toString() + "/GreenEggs-2.txt"); //any .txt file can used here
        Scanner file = new Scanner(f);
        String text = "";
        while(file.hasNext()){
            text += file.next();
        }
        System.out.println(text);
        getOccuringChar(text);
        LinkedHashMap<String,Integer> sorted = new LinkedHashMap<String,Integer>();
        sorted = sortMap(letterCounts);
        System.out.println(sorted);
        letter root = compress(sorted);
        String compressedT = printCompressed(text, root, sorted);
        System.out.println(compressedT);
        System.out.println(decode(compressedT, root));
    }
    
    public static String decode(String c, letter root){
        String compressed = c;
        String original = "";
        letter current = root;
        while(compressed.length() > 0){
            int count = 0;
            current = root;
            while(compressed.substring(count,count+1).equals("1")){
                count++; //the number of 1s before the 0
            }
            for(int i=0; i<count; i++){ //going right through the tree until it hits the parent node of the letter
                current = current.right;
            }
            if(current.left != null && current.right != null){ //then going left
                current = current.left;
            }
            else{
                current = current; //this is if its the last node in the tree
            }
            original += current.letter; //adding the letter to the string
            compressed = compressed.substring(count+1,compressed.length()); //deleting that portion from the compressed string
        }
        return original;
    }
    
    public static String printCompressed(String text, letter root, LinkedHashMap<String,Integer> sorted){
        String comp = "";
        for(int i=0; i<text.length(); i++){
            String lt = text.substring(i,i+1);
            comp += findBinVal(lt, root, sorted); //finding binary value for each letter in string
        }
        return comp;
    }
    
    
    static final int MAX_CHAR = 256;
    static LinkedHashMap<String,Integer> letterCounts = new LinkedHashMap<String,Integer>();
    
    static LinkedHashMap<String,Integer> sortMap(Map<String,Integer> map){ //sorting map into ascending order based on occurences
        LinkedHashMap<String,Integer> sorted = new LinkedHashMap<String,Integer>();
        List<Integer> values = new ArrayList<Integer>(map.values());
        Collections.sort(values);
        for(int i=0; i<values.size(); i++){
            for(Map.Entry<String,Integer> entry : map.entrySet()){
                if(entry.getValue() == values.get(i)){
                    sorted.put(entry.getKey(), entry.getValue());
                    map.remove(entry.getKey());
                    break;
                }
            }
        }
        return sorted;
    }
  
    static void getOccuringChar(String str) {  //i got this code off of stack overflow, gets number of times letter occurs
        // Create an array of size 256 i.e. ASCII_SIZE 
        int count[] = new int[MAX_CHAR]; 
  
        int len = str.length(); 
  
        // Initialize count array index 
        for (int i = 0; i < len; i++){ 
            count[str.charAt(i)]++; 
        }
  
        // Create an array of given String size 
        char ch[] = new char[str.length()]; 
        for (int i = 0; i < len; i++) { 
            ch[i] = str.charAt(i); 
            int find = 0; 
            for (int j = 0; j <= i; j++) { 
                // If any matches found 
                if (str.charAt(i) == ch[j]){  
                    find++; 
                }                
            } 
  
            if (find == 1){ 
                letterCounts.put(String.valueOf(str.charAt(i)), count[str.charAt(i)]);  
            }
        }
    }
    
    
    public static letter compress(LinkedHashMap<String,Integer> list){
        letter root = new letter(null, 0);
        ArrayList<letter> letters = new ArrayList<letter>();
        for(Map.Entry<String,Integer> i : list.entrySet()){ //creating an arraylist of letter objects (nodes)
            letter l = new letter(i.getKey(), i.getValue());
            letters.add(l);
        }
        
        
        while(letters.size() > 1){ 
            letter one = letters.get(0); 
            letter two = letters.get(1);
            if(letters.size() == 3){ //if its an odd sized list
                letter three = letters.get(2);
                letter h = new letter(one.letter + two.letter, one.value + two.value);
                h.set(one, two);
                letter n = new letter(h.letter + three.letter, h.value + three.value);
                n.set(h, three);
                root = n;
                return root;
            }
            else{
                letter n = new letter(one.letter + two.letter, one.value + two.value);
                n.set(one, two); //setting left and right for the combo
                if(letters.size() == 2){ //down to last 2 nodes
                    root = n; //last node is the root
                    return root;
                }
                letters.remove(0); //taking the ones already used out of the arraylist
                letters.remove(0);

                for(int i=0; i<letters.size()-1; i++){ //placing the combo node back into the array
                    if(letters.get(i).value < n.value || letters.get(i).value == n.value){
                        letters.add(i+1, n);
                        break;
                    }
                }
            }
        }
        return root;
    }
    
    public static void printTree(letter root){ //this doesnt really do anything i just wanted to get a look at the tree
        letter current = root;
        while(current.right != null){
            System.out.println(current.letter + current.value + "  left:" + current.left.letter + current.left.value + " right:" + current.right.letter + current.right.value);
            current = current.right;
        }
    }
    
    public static String findBinVal(String l, letter root, LinkedHashMap<String,Integer> s){
        String bin = ""; //binary value
        int occ = s.get(l); //calling back to the hashmap to get the occurence of the letter to navigate tree
        boolean found = false;
        letter current = root; //starting at the root
        
        while(!found){
            if(current.left != null && current.right != null){
                if(current.left.letter.equals(l) && current.left.value == occ){ 
                 bin += "0";
                 found = true;
              }
              else if(current.left.left == null && current.left.right == null){ //for each turn right, adding a 1
                current = current.right;
                bin += "1";
              }
              else if(current.right.left == null && current.right.right == null){
                current = current.left;
                bin += "0";
              }
            }
            else{
                bin += 0;
                found = true;
            }
        }
        return bin;
    }
}
      
   
