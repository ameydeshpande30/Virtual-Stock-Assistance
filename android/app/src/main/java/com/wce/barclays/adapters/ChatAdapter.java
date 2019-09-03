package com.wce.barclays.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wce.barclays.R;
import com.wce.barclays.model.Chat;
import com.wce.barclays.model.Entities;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    private List<Chat> chats;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public ChatAdapter() {
        chats = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return chats.get(position).getSide();
    }

    public  void addText(Entities word)
    {
        chats.add(new Chat(LEFT,word.getOrganization().get(0)));
        notifyItemInserted(chats.size());
    }
    public  void addText(String word, int dir)
    {
        chats.add(new Chat(dir,word));
        notifyItemInserted(chats.size());
    }
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        switch (i)
        {

            case LEFT:
            {
                return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.left_middle_chat_bubble, parent, false));

            }

            case RIGHT:
            {
                return new ChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.right_middle_chat_bubble, parent, false));

            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder chatViewHolder, int i) {
        chatViewHolder.message.setText(chats.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}

class  ChatViewHolder extends RecyclerView.ViewHolder {
    TextView message;
    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message);
    }
}
