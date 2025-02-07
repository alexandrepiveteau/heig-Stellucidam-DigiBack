package ch.heigvd.digiback.ui.data;

import ch.heigvd.digiback.business.api.TaskRunner;
import ch.heigvd.digiback.business.api.auth.Login;
import ch.heigvd.digiback.business.api.auth.iOnTokenFetched;
import ch.heigvd.digiback.ui.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = new LoggedInUser(
            1L,
            "Jane Doe",
            "token");

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public String getToken() {
        return user.getToken();
    }

    public Long getUserId() {
        return user.getUserId();
    }


    public String getUsername() {
        return user.getUsername();
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public LoggedInUser login(String username, String password) throws Exception {
        /* TODO put that back handle login
        Login login = new Login(username, password, new iOnTokenFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(LoggedInUser loggedInUser) {
                setLoggedInUser(loggedInUser);
            }
        });
        return login.call();*/

        final TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeAsync(new Login(username, password, new iOnTokenFetched() {
            @Override
            public void showProgressBar() {

            }

            @Override
            public void hideProgressBar() {

            }

            @Override
            public void setDataInPageWithResult(LoggedInUser loggedInUser) {
                setLoggedInUser(loggedInUser);
            }
        }));

        return user;
    }
}
