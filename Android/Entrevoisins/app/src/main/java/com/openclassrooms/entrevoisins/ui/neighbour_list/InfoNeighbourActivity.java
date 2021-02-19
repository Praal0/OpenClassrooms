package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.events.AddNeighbourgFavorisEvent;
import com.openclassrooms.entrevoisins.events.DeleteNeighbourFavorisEvent;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.openclassrooms.entrevoisins.ui.neighbour_list.MyNeighbourRecyclerViewAdapter.DETAIL_NEIGHBOUR;


public class InfoNeighbourActivity extends AppCompatActivity {

    //Find textView and Image
    private TextView name;
    private TextView phone;
    private TextView auboutMe;
    private TextView adresse;
    private TextView facebook;
    private ImageView imageAvatar;
    private FloatingActionButton btnFavorie;
    private NeighbourApiService mFavApiService;
    private Neighbour neighbour;
    private static final int NOTIF_ID = 123;
    private Toolbar toolbar;
    private boolean favorie;
    private Notification notification;
    private CharSequence verseurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_neighbour);
        mFavApiService = DI.getNeighbourApiService();
        getIncomingIntent();
        fabOnclickListner();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home : {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void getIncomingIntent(){
        neighbour = (Neighbour) getIntent().getSerializableExtra(DETAIL_NEIGHBOUR);
        // Research if we have element in class
        if (neighbour != null){
            name = findViewById(R.id.textViewNameNeighbourg);
            phone = findViewById(R.id.txtPhoneNeighbour);
            auboutMe = findViewById(R.id.txtAboutMe);
            adresse = findViewById(R.id.txtAdresseNeighbour);
            facebook = findViewById(R.id.txtFaceBook);

            // Set Texte and Image
            auboutMe.setText(neighbour.getAboutMe());
            name.setText(neighbour.getName());
            phone.setText(neighbour.getPhoneNumber());
            adresse.setText(neighbour.getAddress());
            facebook.setText("www.Facebook.com/" + name.getText());
            favorie = neighbour.isFavorite();

            imageAvatar = findViewById(R.id.neighbour_Avatar);

            //Set Toolbar and text
            toolbar = findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(neighbour.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            Glide.with(this)
                    .asBitmap()
                    .load(neighbour.getAvatarUrl())
                    .into(imageAvatar);

            btnFavorie = findViewById(R.id.floatingButtonFavorie);
            // We check if Neighbours is favorie or not
            if (favorie) {
                btnFavorie.setImageResource(R.drawable.ic_baseline_star_yellow_24);
                btnFavorie.hide();
                btnFavorie.show();
            } else {
                btnFavorie.setImageResource(R.drawable.ic_baseline_star_border_yellow_24);
                btnFavorie.hide();
                btnFavorie.show();
            }

        }
    }
    
    //Click on favorie button
    private void fabOnclickListner(){
        btnFavorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!favorie){
                    btnFavorie.setImageResource(R.drawable.ic_baseline_star_yellow_24);
                    btnFavorie.hide();
                    btnFavorie.show();
                    addFavoriteNeighbour(v);
                }else{
                    btnFavorie.setImageResource(R.drawable.ic_baseline_star_border_yellow_24);
                    btnFavorie.hide();
                    btnFavorie.show();
                    deleteFavoriteNeighbour(v);
                }
            }
        });
    }

    // Add new Neighbours in favoris list
    private void addFavoriteNeighbour(View view) {

        favorie = true;

        Context context = InfoNeighbourActivity.this;

        EventBus.getDefault().post(new AddNeighbourgFavorisEvent(neighbour));

        Snackbar.make(view, "Vous venez d'ajouter " + name.getText() + " à vos voisins favoris !", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        //Show notification when we add neighbour in favoris list
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), "notify_add");


        mBuilder.setSmallIcon(R.drawable.ic_star_white_24dp);
        mBuilder.setContentTitle("Ajout Favorie");
        mBuilder.setContentText(name.getText() + " fait maintenant partie de vos favoris");
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_add",
                    "Channel add favoris",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(NOTIF_ID, mBuilder.build());

        // After 5 secondes we cancel notification
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mNotificationManager.cancel(NOTIF_ID);
            }
        }, 5000);   //5 seconds
    }

    // Delete Neighbours in favoris list
    private void deleteFavoriteNeighbour(View view) {
        favorie = false;

        Context mContext = InfoNeighbourActivity.this;

        EventBus.getDefault().post(new DeleteNeighbourFavorisEvent(neighbour));

        Snackbar.make(view, "Vous venez de retirer " + name.getText() + " de vos voisins favoris !", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        //Show notification when we remove neighbour in favoris list
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext.getApplicationContext(), "notify_delete");


        mBuilder.setSmallIcon(R.drawable.ic_star_border_white_24dp);
        mBuilder.setContentTitle("Suppression Favorie");
        mBuilder.setContentText(name.getText() + " ne fait plus partie de vos favoris");
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_delete",
                    "Channel delete favoris",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(NOTIF_ID, mBuilder.build());

        // After 5 secondes we cancel notification
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                mNotificationManager.cancel(NOTIF_ID);
            }
        }, 5000);   //5 seconds

    }

    // Use Subscrise for Event (Use when we add or delete Neighbour)
    @Subscribe
    public void DeleteNeighbourFavorisEvent(DeleteNeighbourFavorisEvent event) {
        mFavApiService.deleteFavoriteNeighbour(event.neighbour);
    }

    @Subscribe
    public void AddNeighbourgFavorisEvent(AddNeighbourgFavorisEvent event) {
        mFavApiService.addFavoriteNeighbour(event.neighbour);
    }


}