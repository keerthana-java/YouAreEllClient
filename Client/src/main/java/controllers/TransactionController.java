package controllers;

import models.Id;
import models.Message;

import java.util.List;

public class TransactionController {
    private String rootURL = "http://zipcode.rocks:8085";
    private MessageController msgCtrl;
    private IdController idCtrl;

    public TransactionController(MessageController m, IdController j) {
        idCtrl = j;
        msgCtrl = m;
    }

    public List<Id> getIds() {
        return idCtrl.getIds();
    }

    public String postId(String idtoRegister, String githubName) {
        Id tid = new Id(idtoRegister, githubName);
        tid = idCtrl.postId(tid);
        return ("Id registered.");
    }

    public List<Message> getMessages() {
        return msgCtrl.getMessages();
    }

    public List<Message> getMessagesById(String gitHubId) {

        return msgCtrl.getMessagesForId(new Id("", gitHubId));
    }

    public Message getMessageForSequence(String gitHubId, String seq) {

        return msgCtrl.getMessageForSequence(new Id("", gitHubId), seq);
    }

    public List<Message> getMessagesToIdFromId(String gitHubId, String friendGitHubId) {

        return msgCtrl.getMessagesFromFriend(new Id("", gitHubId), new Id("", friendGitHubId));
    }
}
