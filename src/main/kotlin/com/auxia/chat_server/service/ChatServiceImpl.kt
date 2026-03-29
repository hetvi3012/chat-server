package com.auxia.chat_server.service

import com.auxia.chat.grpc.ChatMessage
import com.auxia.chat.grpc.ChatServiceGrpc
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@GrpcService
class ChatServiceImpl : ChatServiceGrpc.ChatServiceImplBase() {

    // The thread-safe pool of active users
    private val activeObservers = ConcurrentHashMap.newKeySet<StreamObserver<ChatMessage>>()

    override fun joinChat(responseObserver: StreamObserver<ChatMessage>): StreamObserver<ChatMessage> {

        // 1. A new user connects
        activeObservers.add(responseObserver)
        println("A new user joined! Total active users: ${activeObservers.size}")

        // 2. We return a listener to handle incoming messages from this specific user
        return object : StreamObserver<ChatMessage> {

            override fun onNext(message: ChatMessage) {
                println("Received [${message.timestamp}] ${message.sender}: ${message.content}")

                // Broadcast to everyone
                activeObservers.forEach { observer ->
                    try {
                        observer.onNext(message)
                    } catch (e: Exception) {
                        println("Failed to send message to an observer.")
                    }
                }
            }

            override fun onError(t: Throwable) {
                println("A user disconnected due to an error: ${t.message}")
                activeObservers.remove(responseObserver)
            }

            override fun onCompleted() {
                println("A user left the chat cleanly.")
                activeObservers.remove(responseObserver)
                responseObserver.onCompleted()
            }
        }
    }
}