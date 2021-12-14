package youareell;

import controllers.*;
import models.Id;
import models.Message;

import java.util.List;

public class YouAreEll {

    TransactionController tt;

    public YouAreEll (TransactionController t) {
        this.tt = t;
    }

//    public static void main(String[] args) {
//        // hmm: is this Dependency Injection?
//        YouAreEll urlhandler = new YouAreEll(
//            new TransactionController(
//                new MessageController(), new IdController()
//        ));
//        System.out.println(urlhandler.MakeURLCall("/ids", "GET", ""));
//        System.out.println(urlhandler.MakeURLCall("/messages", "GET", ""));
//    }
//
    public String get_ids() {
        return tt.getIds().toString();
    }

    public String create_id(String name, String gitHubId) {
        return tt.postId(name, gitHubId);
    }
//
    public String get_messages() {
        return tt.getMessages().toString();
    }

    public String get_messages_by_id(String gitHubId) {

        return tt.getMessagesById(gitHubId).toString();
    }

    public String get_message_id_then_sequence(String gitHubId, String seq) {

        return tt.getMessageForSequence(gitHubId, seq).toString();
    }

    public String get_messages_to_id_from_id(String gitHubId, String friendGitHubId) {

        return tt.getMessagesToIdFromId(gitHubId, friendGitHubId).toString();
    }


}
