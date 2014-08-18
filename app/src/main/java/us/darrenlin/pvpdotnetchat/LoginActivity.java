package us.darrenlin.pvpdotnetchat;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;

import us.darrenlin.pvpdotnetchat.XMPP.XMPPLogic;
import us.darrenlin.pvpdotnetchat.XMPP.XMPPManager;


public class LoginActivity extends Activity {

    private String host = "chat.na1.lol.riotgames.com";
    private final int PORT = 5223;
    private final String SERVICE = "pvp.net";

    private XMPPManager client;
    private XMPPConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText username = (EditText)findViewById(R.id.editTextUsername);
        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                findViewById(R.id.buttonLogIn).setEnabled(
                        ((EditText)findViewById(R.id.editTextUsername)).getText().length() > 0 &&
                                s.length() > 0);
            }
        });

        EditText password = (EditText)findViewById(R.id.editTextPassword);
        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                findViewById(R.id.buttonLogIn).setEnabled(
                        ((EditText)findViewById(R.id.editTextUsername)).getText().length() > 0 &&
                                s.length() > 0);
            }
        });

        // Init
        SmackAndroid.init(this);
    }

    public void onClickLogIn(View view) {

        String username = ((EditText)findViewById(R.id.editTextUsername)).getText().toString();
        String password = ((EditText)findViewById(R.id.editTextPassword)).getText().toString();

        new AuthenticateUser().execute(username, "AIR_" + password);
    }

    public void onClickServer(View view) {
        boolean checked = ((RadioButton)view).isChecked();

        switch (view.getId()) {
            case R.id.radioButtonNA:
                if (checked) {
                    host = "chat.na1.lol.riotgames.com";
                }
                break;
            case R.id.radioButtonEUW:
                if (checked) {
                    host = "chat.euw1.lol.riotgames.com";
                }
                break;
            case R.id.radioButtonEUNE:
                if (checked) {
                    host = "chat.eun1.lol.riotgames.com";
                }
                break;
            default:
                host = "chat.na1.lol.riotgames.com";
                Toast.makeText(LoginActivity.this, "Invalid server, defaulting to NA", Toast.LENGTH_SHORT).show();
                ((RadioButton)findViewById(R.id.radioButtonNA)).setSelected(true);
        }

        client = new XMPPManager(host, PORT, SERVICE);
        new EstablishConnection().execute(client);
    }

    /**
     * Establishes connection to the server specified in the segmented radio button group
     * This occurs whenever the user chooses a different radio button.
     * Login is disabled while connection is establishing. It restores upon successfully or
     * unsuccessfully connecting to the League chat server.
     */
    private class EstablishConnection extends AsyncTask<XMPPManager, Integer, XMPPConnection> {

        @Override
        protected XMPPConnection doInBackground(XMPPManager... params) {
            connection = (params.length > 0 ? params[0].connect() : client.connect());

            return connection;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            System.out.println("working " + progress[0]);
        }

        @Override
        protected void onPostExecute(XMPPConnection connection) {

            boolean connected = connection != null && connection.isConnected();

            if (connected) {
                XMPPLogic.getInstance().setConnection(connection);
                Toast.makeText(LoginActivity.this, "Connected to server.", Toast.LENGTH_SHORT).show();
            } else {
                XMPPLogic.getInstance().setConnection(null);
                Toast.makeText(LoginActivity.this, "Failed to connect to server.", Toast.LENGTH_SHORT).show();
            }

            findViewById(R.id.editTextUsername).setEnabled(connected);
            findViewById(R.id.editTextPassword).setEnabled(connected);
            findViewById(R.id.checkBoxRememberMe).setEnabled(connected);
        }
    }

    private class AuthenticateUser extends AsyncTask<String, Integer, Roster> {

        @Override
        protected Roster doInBackground(String... params) {
            /*
            [0]: username
            [1]: password
             */
            return client.login(connection, params[0], params[1]);
        }

        @Override
        protected void onPostExecute(Roster roster) {

            if (connection.isAuthenticated() && roster != null) {
                Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, ConversationActivity.class);
                startActivity(intent);

            } else {
                System.out.println("failed to log in, authenticated: " + connection.isAuthenticated());
                Toast.makeText(LoginActivity.this, "Failed to log in. Please check username and password.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
