package com.example.controller;
 
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.service.AccountService;
import com.example.service.MessageService;

import com.example.entity.Account;
import com.example.entity.Message;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> userRegistration(@RequestBody Account account) {
        int accountId_or_message = accountService.userRegistration(account);
        if (accountId_or_message == -2) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        else if (accountId_or_message == -1) {
            return ResponseEntity.status(400).body(null);
        }
        else {
            Account registered = new Account(accountId_or_message, account.getUsername(), account.getPassword());
            return ResponseEntity.ok().body(registered);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> userLogin(@RequestBody Account account) {
        Account user = accountService.userLogin(account.getUsername(), account.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            return ResponseEntity.ok().body(user);
        }
    }
    
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        Message new_message = messageService.postMessage(message);
        if (new_message == null) {
            return ResponseEntity.status(400).body(null);
        }
        else {
            return ResponseEntity.ok().body(new_message);
        }
    }
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        List<Message> messages = messageService.findAllMessages();
        return ResponseEntity.ok().body(messages);
    }
    
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        Message message = messageService.findMessageById(messageId);
        return ResponseEntity.ok().body(message);
    }
    
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId ) {
        int rows_updated = messageService.deleteMessageById(messageId);
        if (rows_updated == 0) {
            return ResponseEntity.ok().body(null);
        }
        else {
            return ResponseEntity.ok().body(1);
        }
    }
    
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> patchMessageById(@PathVariable int messageId, @RequestBody String messageText) {
        int rows_updated = messageService.patchMessageById(messageId, messageText);
        if (rows_updated == 0) {
            return ResponseEntity.status(400).body(null);
        }
        else {
            return ResponseEntity.ok().body(rows_updated);
        }
    }
    
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getUserMessages(@PathVariable int accountId) {
        List<Message> messages = messageService.findAllMessagesByUser(accountId);
        return ResponseEntity.ok().body(messages);
    }
    

}
