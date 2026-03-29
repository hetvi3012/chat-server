package com.auxia.chat_server

import com.auxia.chat.grpc.ChatMessage
import com.auxia.chat.grpc.ChatServiceGrpc
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import java.time.LocalTime
import java.util.concurrent.CountDownLatch

fun main() {
    println("Starting Chat Client...")

    // 1. Build a "channel" (the pipe) to your running local server
    val channel = ManagedChannelBuilder.forAddress("localhost", 9090)
        .usePlaintext() // Tells gRPC not to expect HTTPS encryption for local testing
        .build()

    // 2. Create the asynchronous client "stub"
    val client = ChatServiceGrpc.newStub(channel)

    // We use a latch to stop the program from immediately closing before messages arrive
    val finishLatch = CountDownLatch(1)

    // 3. Define what the CLIENT does when the SERVER sends a message back
    val responseObserver = object : StreamObserver<ChatMessage> {
        override fun onNext(message: ChatMessage) {
            println("📲 CLIENT RECEIVED: [${message.timestamp}] ${message.sender} said: ${message.content}")
        }
        override fun onError(t: Throwable) {
            println("❌ Client Error: ${t.message}")
            finishLatch.countDown()
        }
        override fun onCompleted() {
            println("👋 Server closed the connection.")
            finishLatch.countDown()
        }
    }

    // 4. Knock on the door: call joinChat!
    // This returns a 'requestObserver' which we use to push our own messages up to the server.
    val requestObserver = client.joinChat(responseObserver)

    // 5. Send a couple of fake messages from "Hetvi"
    val message1 = ChatMessage.newBuilder()
        .setSender("Hetvi")
        .setContent("Hello, is anyone there?")
        .setTimestamp(LocalTime.now().toString())
        .build()

    requestObserver.onNext(message1)

    Thread.sleep(1500) // Pause for dramatic effect

    val message2 = ChatMessage.newBuilder()
        .setSender("Hetvi")
        .setContent("Wow, I built my own gRPC client!")
        .setTimestamp(LocalTime.now().toString())
        .build()

    requestObserver.onNext(message2)

    // 6. Tell the server we are leaving the chat
    requestObserver.onCompleted()

    // Keep the app alive just long enough to receive the server's final responses
    finishLatch.await()
    channel.shutdown()
}