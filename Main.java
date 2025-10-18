
public class Main {
    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();
        library.loadData();
        
        if (library.login()) {
            library.displayMenu();
        }
    }
}