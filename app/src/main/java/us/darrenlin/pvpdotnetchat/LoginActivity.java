package us.darrenlin.pvpdotnetchat;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;


public class LoginActivity extends Activity {

    private String host = "chat.na1.lol.riotgames.com";
    private final int PORT = 5223;
    private final String SERVICE = "pvp.net";

    private XmppManager client;
    private XMPPConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                Toast.makeText(LoginActivity.this, "Invalid server, defaulting to NA", Toast.LENGTH_SHORT);
                ((RadioButton)findViewById(R.id.radioButtonNA)).setSelected(true);
        }

        client = new XmppManager(host, PORT, SERVICE);
        new EstablishConnection().execute(client);
    }

    /**
     * Establishes connection to the server specified in the segmented radio button group
     * This occurs whenever the user chooses a different radio button.
     * Login is disabled while connection is establishing. It restores upon successfully or
     * unsuccessfully connecting to the League chat server.
     */
    private class EstablishConnection extends AsyncTask<XmppManager, Integer, XMPPConnection> {

        @Override
        protected XMPPConnection doInBackground(XmppManager... params) {

            connection = (params.length > 0 ? params[0].connect() : client.connect());

            return connection;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            System.out.println("working " + progress[0]);
        }

        @Override
        protected void onPostExecute(XMPPConnection connection) {
            if (connection != null) {
                Toast.makeText(LoginActivity.this, "Connected to server.", Toast.LENGTH_SHORT).show();
            } else {
               Toast.makeText(LoginActivity.this, "Failed to connect to server.", Toast.LENGTH_SHORT).show();
            }
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
                System.out.println("logged in as " + connection.getUser());
                Toast.makeText(LoginActivity.this, "Logged in as " + connection.getUser(), Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("failed to log in, authenticated: " + connection.isAuthenticated());
                Toast.makeText(LoginActivity.this, "Failed to log in. Please check username and password.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
