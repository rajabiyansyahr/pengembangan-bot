
package com.dicoding.menirukanmu;

import com.google.gson.Gson;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.client.LineSignatureValidator;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrofit2.Response;
import java.util.Collections;

import java.util.List;
import java.util.ArrayList;

import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.template.*;
import com.linecorp.bot.model.action.*;
import java.io.IOException;

@RestController
@RequestMapping(value="/linebot")
public class LineBotController
{
    @Autowired
    @Qualifier("com.linecorp.channel_secret")
    String lChannelSecret;
    
    @Autowired
    @Qualifier("com.linecorp.channel_access_token")
    String lChannelAccessToken;

    @RequestMapping(value="/callback", method=RequestMethod.POST)
    public ResponseEntity<String> callback(
        @RequestHeader("X-Line-Signature") String aXLineSignature,
        @RequestBody String aPayload)
    {
        
        final String text=String.format("The Signature is: %s",
            (aXLineSignature!=null && aXLineSignature.length() > 0) ? aXLineSignature : "N/A");
        System.out.println(text);
        
        final boolean valid=new LineSignatureValidator(lChannelSecret.getBytes()).validateSignature(aPayload.getBytes(), aXLineSignature);
        System.out.println("The signature is: " + (valid ? "valid" : "tidak valid"));
        if(aPayload!=null && aPayload.length() > 0)
        {
            System.out.println("Payload: " + aPayload);
        }
        Gson gson = new Gson();
        Payload payload = gson.fromJson(aPayload, Payload.class);

        String msgText = " ";
        String idTarget = " ";
        String eventType = payload.events[0].type;
        String welcomeMsg = "Terimakasih sudah menambahkan Assalam Polban di kontak kamu! :)\n\nNantikan terus informasi seputar dunia islam dan kegiatan-kegiatan Assalam Polban lainnya disini.\n\nUntuk mendapatkan jadwal adzan hari ini, silahkan masukkan format:\nbot informasi adzan\n\n\n#SatuDalamIslam\n#PolbanMadani2017";

        if (eventType.equals("join")){
            if (payload.events[0].source.type.equals("group")){ //ketika bot di invite join ke dalam group 
                replyToUser(payload.events[0].replyToken, welcomeMsg);
                try{
                    List<Action> actions = new ArrayList<Action>();
                    Action action = new URIAction("Instagram", "https://www.instagram.com/assalampolban/");
                    Action action2 = new URIAction("Twitter", "https://www.twitter.com/assalampolban/");
                    Action action3 = new URIAction("Facebook", "https://www.facebook.com/assalampolban/");
                    Action action4 = new URIAction("LINE", "https://line.me/R/ti/p/%40LTA5871H");
                    actions.add(action);
                    actions.add(action2);
                    actions.add(action3);
                    actions.add(action4);
                    Template temp = new ButtonsTemplate("https://lh3.ggpht.com/Su-kBS_TEjK9ISAcAPNWMHL0OCNyiP56aeB5czxCqxgg3KrPfqL4qcRLJvwBjWummw=h310","Assalam Polban","Silahkan Add/Follow sosial media kita yang lainnya :)", actions);
                    TemplateMessage tempMsg = new TemplateMessage("Assalamu\'alaikum Wr. Wb.", temp);
                    sendButtonTempalte(tempMsg, payload.events[0].source.groupId );
                } catch (Exception e) {
                    System.out.println("Exception is raised ");
                    e.printStackTrace();
                }
            } else if (payload.events[0].source.type.equals("room")){ //ketika bot di invite join ke dalam multi-chat
                replyToUser(payload.events[0].replyToken, welcomeMsg);
                try{
                    List<Action> actions = new ArrayList<Action>();
                    Action action = new URIAction("Instagram", "https://www.instagram.com/assalampolban/");
                    Action action2 = new URIAction("Twitter", "https://www.twitter.com/assalampolban/");
                    Action action3 = new URIAction("Facebook", "https://www.facebook.com/assalampolban/");
                    Action action4 = new URIAction("LINE", "https://line.me/R/ti/p/%40LTA5871H");
                    actions.add(action);
                    actions.add(action2);
                    actions.add(action3);
                    actions.add(action4);
                    Template temp = new ButtonsTemplate("https://lh3.ggpht.com/Su-kBS_TEjK9ISAcAPNWMHL0OCNyiP56aeB5czxCqxgg3KrPfqL4qcRLJvwBjWummw=h310","Assalam Polban","Silahkan Add/Follow sosial media kita yang lainnya :)", actions);
                    TemplateMessage tempMsg = new TemplateMessage("Assalamu\'alaikum Wr. Wb.", temp);
                    sendButtonTempalte(tempMsg, payload.events[0].source.roomId );
                } catch (Exception e) {
                    System.out.println("Exception is raised ");
                    e.printStackTrace();
                }
            }
        } else if (eventType.equals("message")){ // interaksi pesan dengan user
            if (payload.events[0].source.type.equals("group")){
                idTarget = payload.events[0].source.groupId;
            } else if (payload.events[0].source.type.equals("room")){
                idTarget = payload.events[0].source.roomId;
            } else if (payload.events[0].source.type.equals("user")){
                idTarget = payload.events[0].source.userId;
            }

            if (!payload.events[0].message.type.equals("text")){
                replyToUser(payload.events[0].replyToken, "Unknown message");
            } else {
                msgText = payload.events[0].message.text;
                msgText = msgText.toLowerCase();
                String[] keyWords = msgText.split(" ");
        
                if (!msgText.contains("bot leave now")){
                    if(keyWords[0].equals("bot")){
                        if(keyWords[1].equals("informasi")){
                            if(keyWords[2].equals("adzan")){
                                try{
                                    List<Action> actions = new ArrayList<Action>();
                                    Action action1 = new URIAction("Yuk Shalat! :)", "http://google.com");
                                    actions.add(action1);
                                    CarouselColumn cColumn1 = new CarouselColumn("https://i2.wp.com/dalamruangkeheningan.files.wordpress.com/2017/05/1.jpg?w=463&h=307&crop&ssl=1","Waktu Shubuh","04:30 am", actions);
                                    CarouselColumn cColumn2 = new CarouselColumn("https://i0.wp.com/dalamruangkeheningan.files.wordpress.com/2017/05/2.jpg?w=229&h=152&crop&ssl=1","Waktu Dzuhur","11:45 am", actions);
                                    CarouselColumn cColumn3 = new CarouselColumn("https://i2.wp.com/dalamruangkeheningan.files.wordpress.com/2017/05/3.jpg?w=229&h=151&crop&ssl=1","Waktu Ashar","03:07 pm", actions);
                                    CarouselColumn cColumn4 = new CarouselColumn("https://i2.wp.com/dalamruangkeheningan.files.wordpress.com/2017/05/4.jpg?w=346&h=229&crop&ssl=1","Waktu Maghrib","05:48 pm", actions);
                                    CarouselColumn cColumn5 = new CarouselColumn("https://i0.wp.com/dalamruangkeheningan.files.wordpress.com/2017/05/5.jpg?w=346&h=229&crop&ssl=1","Waktu Isya","06:58 pm", actions);
                                    List<CarouselColumn> cColumns = new ArrayList<CarouselColumn>();
                                    cColumns.add(cColumn1);
                                    cColumns.add(cColumn2);
                                    cColumns.add(cColumn3);
                                    cColumns.add(cColumn4);
                                    cColumns.add(cColumn5);
                                    Template temp = new CarouselTemplate(cColumns);
                                    TemplateMessage tempMsg = new TemplateMessage("Jadwal Adzan", temp);
                                    sendButtonTempalte(tempMsg, idTarget);
                                } catch (Exception e) {
                                    System.out.println("Exception is raised ");
                                    e.printStackTrace();
                                }           
                            } else if(keyWords[2].equals("adzan2")){
                                try{
                                    List<Action> actions = new ArrayList<Action>();
                                    Action action1 = new URIAction("Subuh 04:33 am", "http://google.com");
                                    Action action2 = new URIAction("Dzuhur 11:49 am", "http://google.com");
                                    Action action3 = new URIAction("Ashar 03:10 pm", "http://google.com");
                                    actions.add(action1);
                                    actions.add(action2);
                                    actions.add(action3);
                
                                    // List<Action> actionz = new ArrayList<Action>();
                                    // Action action4 = new URIAction("Maghrib 05:44 am", "http://google.com");
                                    // Action action5 = new URIAction("Isya 06:55 am", "http://google.com");
                                    // actionz.add(action4);
                                    // actionz.add(action5);
                                    // List<Action> actiont = new ArrayList<Action>();
                                    // Action action6 = new URIAction("Dhuha 08:00 am", "http://google.com");
                                    // Action action7 = new URIAction("Tahajud 01:00 am", "http://google.com");
                                    // actiont.add(action6);
                                    // actiont.add(action7);
                                    
                                    CarouselColumn cColumn1 = new CarouselColumn("https://i2.wp.com/dalamruangkeheningan.files.wordpress.com/2017/05/1.jpg?w=463&h=307&crop&ssl=1","Jadwal Shalat","Lokasi : Bandung dan Sekitarnya", actions);
                                    CarouselColumn cColumn2 = new CarouselColumn("https://i0.wp.com/dalamruangkeheningan.files.wordpress.com/2017/05/2.jpg?w=229&h=152&crop&ssl=1","Jadwal Shalat","Lokasi : Bandung dan Sekitarnya", actions);
                                    CarouselColumn cColumn3 = new CarouselColumn("https://i2.wp.com/dalamruangkeheningan.files.wordpress.com/2017/05/3.jpg?w=229&h=151&crop&ssl=1","Jadwal Shalat","Lokasi : Bandung dan Sekitarnya", actions);
                                    List<CarouselColumn> cColumns = new ArrayList<CarouselColumn>();
                                    cColumns.add(cColumn1);
                                    cColumns.add(cColumn2);
                                    cColumns.add(cColumn3);
                                    Template temp = new CarouselTemplate(cColumns);
                                    TemplateMessage tempMsg = new TemplateMessage("Jadwal Adzan", temp);
                                    sendButtonTempalte(tempMsg, idTarget);
                                } catch (Exception e) {
                                    System.out.println("Exception is raised ");
                                    e.printStackTrace();
                                }           
                            }
                        }
                    }
                } else {
                    if (payload.events[0].source.type.equals("group")){
                        leaveGR(payload.events[0].source.groupId, "group");
                    } else if (payload.events[0].source.type.equals("room")){
                        leaveGR(payload.events[0].source.roomId, "room");
                    }
                }

            }
        }
        else if (eventType.equals("follow")){ // ketika OA bot di-follow secara pribadi
            replyToUser(payload.events[0].replyToken, welcomeMsg);
            try{
                List<Action> actions = new ArrayList<Action>();
                Action action = new URIAction("Instagram", "https://www.instagram.com/assalampolban/");
                Action action2 = new URIAction("Twitter", "https://www.twitter.com/assalampolban/");
                Action action3 = new URIAction("Facebook", "https://www.facebook.com/assalampolban/");
                Action action4 = new URIAction("LINE", "https://line.me/R/ti/p/%40LTA5871H");
                actions.add(action);
                actions.add(action2);
                actions.add(action3);
                actions.add(action4);
                Template temp = new ButtonsTemplate("https://lh3.ggpht.com/Su-kBS_TEjK9ISAcAPNWMHL0OCNyiP56aeB5czxCqxgg3KrPfqL4qcRLJvwBjWummw=h310","Assalam Polban","Silahkan Add/Follow sosial media kita yang lainnya :)", actions);
                TemplateMessage tempMsg = new TemplateMessage("Assalamu\'alaikum Wr. Wb.", temp);
                sendButtonTempalte(tempMsg, payload.events[0].source.useId );
            } catch (Exception e) {
                System.out.println("Exception is raised ");
                e.printStackTrace();
            }
        } 
         
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    private void getMessageData(String message, String targetID) throws IOException{
        if (message!=null){
            pushMessage(targetID, message);
        }
    }

    private void replyToUser(String rToken, String messageToUser){
        TextMessage textMessage = new TextMessage(messageToUser);
        ReplyMessage replyMessage = new ReplyMessage(rToken, textMessage);
        try {
            Response<BotApiResponse> response = LineMessagingServiceBuilder
                .create(lChannelAccessToken)
                .build()
                .replyMessage(replyMessage)
                .execute();
            System.out.println("Reply Message: " + response.code() + " " + response.message());
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }

    private void sendButtonTempalte(TemplateMessage message, String to){
        PushMessage pushMessage = new PushMessage(to,message);
        try {
            Response<BotApiResponse> response = LineMessagingServiceBuilder
            .create(lChannelAccessToken)
            .build()
            .pushMessage(pushMessage)
            .execute();
            System.out.println(response.code() + " " + response.message());
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }

    private void pushMessage(String sourceId, String txt){
        TextMessage textMessage = new TextMessage(txt);
        PushMessage pushMessage = new PushMessage(sourceId,textMessage);
        try {
            Response<BotApiResponse> response = LineMessagingServiceBuilder
            .create(lChannelAccessToken)
            .build()
            .pushMessage(pushMessage)
            .execute();
            System.out.println(response.code() + " " + response.message());
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }

    private void leaveGR(String id, String type){
        try {
            if (type.equals("group")){
                Response<BotApiResponse> response = LineMessagingServiceBuilder
                    .create(lChannelAccessToken)
                    .build()
                    .leaveGroup(id)
                    .execute();
                System.out.println(response.code() + " " + response.message());
            } else if (type.equals("room")){
                Response<BotApiResponse> response = LineMessagingServiceBuilder
                    .create(lChannelAccessToken)
                    .build()
                    .leaveRoom(id)
                    .execute();
                System.out.println(response.code() + " " + response.message());
            }
        } catch (IOException e) {
            System.out.println("Exception is raised ");
            e.printStackTrace();
        }
    }
}
