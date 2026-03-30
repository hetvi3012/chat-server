package com.auxia.chat_server.entity

import jakarta.persistence.*

@Entity
@Table(name = "chat_messages")
data class ChatMessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val sender: String,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val timestamp: String
)