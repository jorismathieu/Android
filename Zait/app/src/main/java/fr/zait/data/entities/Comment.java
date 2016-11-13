package fr.zait.data.entities;

import java.util.List;

public class Comment {
    public String author;
    public String body;
    public int score;
    public long createdUtc;
    public int nbAnswers;
    public List<Comment> answers;
}
