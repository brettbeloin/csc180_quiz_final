package com.csc180.brettbeloin.models;

import java.util.List;

//sample record:  "question":"Which of the following languages is used as a scripting language in the Unity 3D game engine?","correct_answer":"C#","incorrect_answers":["Java","C++","Objective-C"]}
//{"type":"multiple","difficulty":"hard","category":"Mythology","question":"Who is a minor god that is protector and creator of various arts, such as cheese making and bee keeping.","correct_answer":"Aristaeus","incorrect_answers":["Autonoe","Carme","Cephisso"]}
public record Question(String type, String difficulty, String category, String question, String correct_answer,
        List<String> incorrect_answers) {
}
