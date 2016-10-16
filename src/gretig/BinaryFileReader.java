package gretig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jarre on 8/10/2016.
 */
public class BinaryFileReader {

    //Maybe read byte per byte is more efficient?
    public byte[] readBinaryFile(String filename){
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "data", "klein", filename);
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Failed to read the given binary file: " + filename);
        }
        return null;
    }

    public List<Graph> readByteArray(byte[] bytes){
        //printBytes(bytes);
        //First 7 numbers are the header + 1 byte for number byte length.
        assert(bytes.length>=8);
        int i;
        String header = "";
        for(i=0; i<7; i++){
            header += (char) bytes[i];
        }
        //Header must be >>SEC<<
        assert(header.equals(">>SEC<<"));
        List<Graph> graphs = new ArrayList<Graph>();
        printBytes(bytes);

        while(i<bytes.length) {
            //Read a single graph.
            int numberLength = (int) bytes[i++];
            //Amount of nodes
            int nodeAmount = readNumber(bytes, i, numberLength);
            //Amount of vertices
            int totalVertices = readNumber(bytes, i + numberLength, numberLength);
            Graph graph = new Graph(nodeAmount, totalVertices);
            int nodeIndex = 0;
            Node[] nodes = new Node[nodeAmount];
            nodes[0] = new Node(1);
            int verticesAmount = 0;
            i += 2 * numberLength;
            for (; i < bytes.length && nodeIndex < nodeAmount; i += numberLength) {
                int number = readNumber(bytes, i, numberLength);
                if (number == 0) {
                    nodes[nodeIndex].setEdgesAmount(verticesAmount);
                    verticesAmount = 0;
                    graph.addNode(nodes[nodeIndex]);
                    nodeIndex++;
                    if(nodeIndex!=nodeAmount){
                        nodes[nodeIndex] = new Node(nodeIndex+1);
                    }
                } else {
                    verticesAmount++;
                    graph.addVertex(nodes[nodeIndex], number);
                }
            }
            graphs.add(graph);
        }
        return graphs;
    }

    public void printBytes(byte[] bytes){
        System.out.println(Arrays.toString(bytes));
    }

    private int readNumber(byte[] bytes, int index, int length){
        int exp = 1;
        int number = 0;
        for(int k=index; k<index+length; k++){
            number += ((int) bytes[k])*exp;
            exp*=256;
        }
        return number;
    }
}