package gretig;

import graph.Edge;
import graph.Graph;
import graph.Node;

import input.BinaryInputReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jarre on 8/10/2016.
 */
public class Dominantie {
    //Algoritme uitvoeren op insertion sorted list instead of nodearray & remove := index = null.
    public static List<Node> getDominantList(Graph graph){
        graph.sort();
        List<Node> dominantList = new ArrayList<>();
        int coverage = 0;
        int size = graph.size();
        while(coverage < size){
            Node v = graph.pull();
            dominantList.add(v);
            if(!v.visited()){
                v.visit();
                coverage++;
            }
            for(Edge edge: v.getEdges()){
                Node w = edge.getNeighbour(v);
                if(!w.visited()){
                    w.visit();
                    coverage++;
                }
            }
        }
        return dominantList;
    }

    public static boolean isDominant(Graph g, List<Node> dominantList){
        Node[] graphNodes = g.getNodes();
        Set<Node> coverage = new HashSet<>();
        for(Node node: dominantList){
            coverage.add(node);
            for(Edge edge: node.getEdges()){
                coverage.add(edge.getNeighbour(node));
            }
        }
        return graphNodes.length == coverage.size();
    }

    public static void main(String[] args) {
        BinaryInputReader reader = new BinaryInputReader();
        try {
            List<Graph> graphs = reader.readByteArray();
            for(Graph graph: graphs){
                List<Node> dominantList = getDominantList(graph);
                int nodesSize = graph.getNodes().length;
                int dominantlistSize = dominantList.size();
                System.out.print(dominantlistSize + "/" + nodesSize + " (" + ((double)dominantlistSize/(double)nodesSize)*100 + "%): ");
                /*
                for(Node node: dominantList){
                    System.out.print(node.getNumber());
                    System.out.print(" ");
                }
                System.out.println();
                */
            }
        } catch (IOException e) {
            System.err.println("Couldn't read the given input.");
        }
    }
}

