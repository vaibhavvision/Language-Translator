package com.translatealll.anguagesapp.database;

import java.util.List;

public interface Downloadedlngs_dao {
    void InsertDownloaded_lngs(DownloadedLngsTable downloadedLngs_table);

    List<DownloadedLngsTable> SelectDownloadedLngs();

    int UpdateLngStatus(String status, String downloadedlng_name);

    int delete(String item);

    int deletechat(ChatTable ct);

    int deleteword(WordsHistoryTable lt);

    String[] getSavedChats();

    long insert_chat(ChatTable ct);

    long insert_lngs(WordsHistoryTable qr);

    ChatTable[] selectallchats();

    WordsHistoryTable[] selectalllngs();

    List<ChatTable> specificchat(String chatname);
}
