package id;

import java.util.Random;

public class IdGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ID_LENGTH = 8;
    private String id = "";

    public IdGenerator() {
        Random random = new Random();
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            this.id+=CHARACTERS.charAt(index);
        }
    }

    public String getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return this.id;
    }
}


/*
    Driver Code

    package id;

    public class driver {
        public static void main(String[] args) {
            IdGenerator id = new IdGenerator();
            System.out.println(id.getId());
        }
    }


*/