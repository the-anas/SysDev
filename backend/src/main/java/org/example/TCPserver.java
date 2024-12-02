package org.example;

import java.io.*;
import java.net.*;

public class TCPserver {
    public static void main(String[] args) throws IOException {

        // graphing
        Graph graph=Graph.graphing();

        try (ServerSocket serverSocket = new ServerSocket(1234)) {

            //System.out.println("Server started, waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                //System.out.println("New client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, graph);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final Graph graph;

    public ClientHandler(Socket clientSocket, Graph graph) {
        this.clientSocket = clientSocket;
        this.graph = graph;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                //RIGHT HERE; DEPENDING ON RECEIVED REQUEST, WE WILL DECIDE TO RUN DIJKSTRA OR ASTAR CODE
                String[] words = request.split("\\s+");

                if (words[0].equals("dijkstra")){
                    //code for executing dijkstra
                    String geojson = AnyLocation.shortest_path_dij(Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3]), Double.parseDouble(words[4]));
                    out.println(geojson);
                    out.println();
                }

                else if (words[0].equals("astar")){
                    //code for  astar
                    String geojson = AnyLocation.shortest_path_astar(Double.parseDouble(words[1]), Double.parseDouble(words[2]), Double.parseDouble(words[3]), Double.parseDouble(words[4]));
                    out.println(geojson);
                    out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}


