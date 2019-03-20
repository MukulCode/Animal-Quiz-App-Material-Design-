package com.example.mortezasaadat.animalquiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {


    public static final String  GUESSES = "settings_numberOfGuesses";
    public static final String ANIMALS_TYPE = "settings_animalsType";
    public static final String QUIZ_BACKGROUND_COLOR = "settings_quiz_background_color";
    public static final String QUIZ_FONT = "settings_quiz_font";


    private boolean isSettingsChanged = false;


    static Typeface chunkfive;
    static Typeface fontlerybrown;
    static Typeface wonderbarDemo;


    MainActivityFragment myAnimalQuizFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        chunkfive = Typeface.createFromAsset(getAssets(), "fonts/Chunkfive.otf");
        fontlerybrown = Typeface.createFromAsset(getAssets(), "fonts/FontleroyBrown.ttf");
        wonderbarDemo = Typeface.createFromAsset(getAssets(), "fonts/Wonderbar Demo.otf");


        PreferenceManager.setDefaultValues(MainActivity.this, R.xml.quiz_preferences, false);


        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).
                registerOnSharedPreferenceChangeListener(settingsChangeListener);


        myAnimalQuizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.animalQuizFragment);

        myAnimalQuizFragment.modifyAnimalsGuessRows(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
        myAnimalQuizFragment.modifyTypeOfAnimalsInQuiz(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
        myAnimalQuizFragment.modifyQuizFont(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
        myAnimalQuizFragment.modifyBackgroundColor(PreferenceManager.getDefaultSharedPreferences(MainActivity.this));
        myAnimalQuizFragment.resetAnimalQuiz();
        isSettingsChanged = false;






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Intent preferencesIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);

    }

    private SharedPreferences.OnSharedPreferenceChangeListener settingsChangeListener
            = new SharedPreferences.OnSharedPreferenceChangeListener() {

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {


            isSettingsChanged = true;

            if (key.equals(GUESSES)) {

                myAnimalQuizFragment.modifyAnimalsGuessRows(sharedPreferences);
                myAnimalQuizFragment.resetAnimalQuiz();

            } else if (key.equals(ANIMALS_TYPE)) {

                Set<String> animalTypes = sharedPreferences.getStringSet(ANIMALS_TYPE, null);

                if (animalTypes != null && animalTypes.size() > 0) {

                    myAnimalQuizFragment.modifyTypeOfAnimalsInQuiz(sharedPreferences);
                    myAnimalQuizFragment.resetAnimalQuiz();

                } else {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    animalTypes.add(getString(R.string.default_animal_type));
                    editor.putStringSet(ANIMALS_TYPE, animalTypes);
                    editor.apply();

                    Toast.makeText(MainActivity.this,
                            R.string.toast_message, Toast.LENGTH_SHORT).show();

                }

            } else if (key.equals(QUIZ_FONT)) {

                myAnimalQuizFragment.modifyQuizFont(sharedPreferences);
                myAnimalQuizFragment.resetAnimalQuiz();
            } else if (key.equals(QUIZ_BACKGROUND_COLOR)) {

                myAnimalQuizFragment.modifyBackgroundColor(sharedPreferences);
                myAnimalQuizFragment.resetAnimalQuiz();

            }

            Toast.makeText(MainActivity.this, R.string.change_message, Toast.LENGTH_SHORT).show();




        }
    };

}
