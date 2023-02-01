package com.example.teste_multijoagdor;

import static com.example.teste_multijoagdor.R.id.editText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

public class MainActivity extends AppCompatActivity {

EditText editText;
Button button;

String PlayerName = "";

FirebaseDatabase database;
DatabaseReference PlayerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText); //erro
        button = findViewById(R.id.button);

        database = FirebaseDatabase.getInstance();

        //Checar existencia do jogador
        SharedPreferences preferences = getSharedPreferences("Prefs", 0);
        PlayerName = preferences.getString("PlayerName", "");
        if(!PlayerName.equals("")){
            PlayerRef = database.getReference("players/" + PlayerName);
            addEventListener();
            PlayerRef.setValue("");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Entrada do jogador no
                PlayerName = editText.getText().toString();
                editText.setText("");
                if(!PlayerName.equals("")){
                    button.setText("Logging in");
                    button.setEnabled(false);
                    PlayerRef = database.getReference("players/" + PlayerName);
                    addEventListener();
                    PlayerRef.setValue("");
                }
            }
        });
    }
    private void  addEventListener(){
        //ler as informações do Database
        PlayerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Sucesso + continua
                if (!PlayerName.equals("")){
                    SharedPreferences preferences = getSharedPreferences("Prefs", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("PlayerName", PlayerName);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), MainActivity2.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Caso de error
                button.setText("Log In");
                button.setEnabled(true);
                Toast.makeText(MainActivity.this, "ERROR!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}