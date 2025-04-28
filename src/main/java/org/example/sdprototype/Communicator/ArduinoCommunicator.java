package org.example.sdprototype.Communicator;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoCommunicator {
    private final SerialPort serialPort;

    public ArduinoCommunicator(String portDescriptor) {
        serialPort = SerialPort.getCommPort(portDescriptor);
        serialPort.setBaudRate(9600);
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        serialPort.setParity(SerialPort.NO_PARITY);

        if (serialPort.openPort()) {
            System.out.println("Port opened successfully");
        }
        else {
            System.out.println("Port failed to open");
        }
    }

    // Method to communicate index of space player is moving to and, if landed on a special space,
    // the final target index (e.g. if a player lands on a special space and then needs to move back one)
    public void sendIndices(int initialTargetIndex, int finalTargetIndex) {
        if (serialPort != null && serialPort.isOpen()) {
            // Send data to arduino in the following format: "MOVE,initial,final\n"
            String data = "MOVE," + initialTargetIndex + "," + finalTargetIndex + "\n";
            byte[] bytes = data.getBytes();
            serialPort.writeBytes(bytes, bytes.length);
            System.out.println("Sent move data to Arduino: " + data.trim());
        }
    }

    // Method to communicate the game mode to the arduino
    public void sendGameMode(int gameMode) {
        if (serialPort != null && serialPort.isOpen()) {
            // Send data to arduino in the following format: "GAMEMODE,mode\n"
            String data = "GAMEMODE," + gameMode + "\n";
            byte[] bytes = data.getBytes();
            serialPort.writeBytes(bytes, bytes.length);
            System.out.println("Sent move data to Arduino: " + data.trim());
        }
    }

    // Method to close connection
    public void close() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }
    }
}
