/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import com.mycompany.chatjaxb.classes.Message;
import com.mycompany.chatjaxb.classes.Room;
import com.mycompany.chatjaxb.classes.User;
import controller.Controller;
import jakarta.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adryc
 */
public class Utils {

    String uri;
    Room room;
    Message m;
    boolean stopChat = false;
    File f;
    ArrayList<Message> messages;
    ArrayList<Message> messagesfromdeleteduser;
    ArrayList<User> users;
    Controller c;

    public Utils() {

    }

    public void execute() throws JAXBException {
        System.out.println("¡Bienvenido a JAXBChat!");

        uri = uriValidator(); //Se comprueba que el usuario meta un nombre o uri válidos

        f = new File(uri);

        c = new Controller(uri);

        //Si el archivo ya existe, se recupera toda su información
        //Si no, se crea un nuevo Room y se guarda en un archivo xml nuevo
        if (!f.exists()) {
            room = new Room();
            messages = new ArrayList<>();
            users = new ArrayList<>();
            c.save(room);
            System.out.println("Este es el inicio de la sala de chat");
        }

        //Se recupera la información al iniciar programa, para que los nuevos
        //usuarios no provoquen una pérdida de datos
        room = c.read();
        messages = readMessages(room);
        users = readUsers(room);
        showMessages(room); //Antes del registro, se muestra el historial
        //de mensajes

        //Se registra al usuario y se actualiza el Room del xml,
        //volviendo a recoger los datos después
        User user = register(users, uri);
        room = c.read();
        messages = readMessages(room);
        users = readUsers(room);
        users.add(user);
        room.setUsers(users);
        c.save(room);
        room = c.read();
        users = readUsers(room);
        messages = readMessages(room);

        //Los usuarios pueden seguir escribiendo o refrescando el historial
        //de mensajes hasta que decidan salir
        //Se actualiza el Room y se comprueban sus datos antes de cada mensaje
        //para evitar que se pierda información
        while (stopChat == false) {
            room = c.read();
            users = readUsers(room);
            messages = readMessages(room);
            stopChat = chat(user, messages, users, room, c);

        }

        //Se recuperan los datos una última vez, se elimina al usuario de la lista
        //y se guardan todos los cambios en el archivo xml
        room = c.read();
        messages = readMessages(room);
        users = readUsers(room);
        users = deleteUser(room, user.getNickname());
        room.setUsers(users);
        try {
            c.save(room);
        } catch (JAXBException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("¡Vuelve pronto!");

    }

//Muestra por pantalla todos los mensajes
    public static void showMessages(Room room) {
        for (Message m : room.getMessages()) {
            System.out.println(m.toString());
        }

    }

//Recupera la lista de usuarios alojados en el Room del xml
//El room llega actualizado de la función execute()
    public static ArrayList<User> readUsers(Room room) {
        ArrayList<User> result = new ArrayList<>();
        for (User u : room.getUsers()) {
            result.add(u);
        }

        return result;

    }

//Recupera la lista de mensajes alojados en el Room del xml
    public static ArrayList<Message> readMessages(Room room) {
        ArrayList<Message> result = new ArrayList<>();
        for (Message m : room.getMessages()) {
            result.add(m);
        }

        return result;

    }

//Se registra y guarda al nuevo usuario
//También se comprueba que su nombre no coincida con ningún usuario activo
    public static User register(ArrayList<User> users, String uri) throws JAXBException {
        String name = "";
        boolean exists = true;
        int i;
        Controller c = new Controller(uri);

        while (exists) {
            i = 0;
            Scanner sc = new Scanner(System.in);
            System.out.println("Ingrese su nombre de usuario: ");
            name = sc.nextLine();
            Room room = c.read();
            users = readUsers(room);
            for (User u : users) {
                if (u.getNickname().equals(name)) {
                    i++;
                    System.out.println("Ese nombre de usuario ya existe");
                }
            }
            if (i == 0) {
                exists = false;
            } else {
                exists = true;
            }
        }
        User u = new User(name);
        System.out.println("¡Bienvenido a la sala " + u.getNickname() + "!");
        return u;
    }

//Se solicita al usuario que envíe un mensaje o acceda al historial actualizado
//de estos hasta que decida cerrar el programa
    public static boolean chat(User u, ArrayList<Message> messages, ArrayList<User> users, Room room, Controller c) throws JAXBException {

        boolean stop;
        int n;
        String validator = "1";
        Scanner sc = new Scanner(System.in);
        String message;
        messages = c.getMessages(room);
        users = c.getUsers(room);

        Message m;
        while (!validator.equals("0")) {

            System.out.println("Escriba un mensaje; ingrese 0 para salir o 1 para leer los mensajes: ");
            message = sc.nextLine();
            if (message.equals("0")) {
                validator = "0";
            } else if (message.equals("1")) {//Devuelve todos los mensajes, antiguos y nuevos
                room = c.read();
                users = c.getUsers(room);
                System.out.println("Usuarios activos: ");
                for(User user : users){
                    System.out.println("- "+user.getNickname());
                }
                showMessages(room);
                
            } else {
                room = c.read();
                users = c.getUsers(room);
                messages = c.getMessages(room);
                m = new Message(u, message);
                messages.add(m);
                room.setMessages(messages);
                c.save(room);

                System.out.println(m.toString());
            }
        }

        stop = true;
        return stop;
    }

//Elimina al usuario, devolviendo la lista actualizada tras esta acción
    public static ArrayList<User> deleteUser(Room room, String nickname) {
        ArrayList<User> users = room.deleteUser(nickname);
        return users;
    }

//Se comprueba que la dirección o nombre del archivos tengan, al menos, un formato
//*.xml
    public static String uriValidator() {
        boolean urivalidator = false;
        String uri = "";
        while (!urivalidator) {
            System.out.println("Introduce la dirección dónde se guardará el xml generado, o simplemente su nombre, seguido de .xml al final: ");
            Scanner sc = new Scanner(System.in);
            uri = sc.nextLine();

            if (uri.length() < 4) {
                System.out.println("URI mal introducida (Ejemplo: a.xml)");
            } else {//Se comprueban los últimos 4 carácteres de forma que cumplan el formato
                char l = uri.charAt(uri.length() - 1);
                char m = uri.charAt(uri.length() - 2);
                char x = uri.charAt(uri.length() - 3);
                char point = uri.charAt(uri.length() - 4);
                if (l == 'l' && m == 'm' && x == 'x' && point == '.') {
                    urivalidator = true;
                } else {
                    System.out.println("URI mal introducida (Ejemplo: a.xml)");
                }
            }

        }
        return uri;
    }
}
