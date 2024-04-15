import CssBaseline from "@mui/material/CssBaseline";
import { ThemeProvider, createTheme } from "@mui/material/styles";
import moment from "moment";
import "moment-timezone";
import React from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import PrivateRoute from "./components/PrivateRoute";
import Layout from "./components/layout/Layout";
import { AuthProvider } from "./contexts/AuthContext";
import AdminDashboard from "./pages/admin/AdminDashboard";
import AdminLogin from "./pages/admin/AdminLogin";
import AdminRecipeCommentsList from "./pages/admin/AdminRecipeCommentsList";
import AdminRecipesList from "./pages/admin/AdminRecipesList";
import UsersList from "./pages/admin/UsersList";
import PageNotFound from "./pages/home/NotFound";
import Logout from "./pages/login/Logout";
import CreateRecipe from "./pages/recipes/CreateRecipe";
import MyRecipesList from "./pages/recipes/MyRecipesList";
import RecipeDetails from "./pages/recipes/RecipeDetails";
import RecipesList from "./pages/recipes/RecipesList";
import SubscriptionPage from "./pages/recipes/SubscriptionPage";
import UpdateRecipe from "./pages/recipes/UpdateRecipe";
import UserDashboard from "./pages/users/UserDashboard";
import UserLogin from "./pages/users/UserLogin";
import UserProfile from "./pages/users/UserProfile";
import UserRegister from "./pages/users/UserRegister";

moment.tz.setDefault("UTC");

const theme = createTheme({
  palette: {
    primary: {
      main: "#46A462",
    },
    secondary: {
      main: "#D51F1F",
    },
  },
});

function App() {
  return (
    <AuthProvider>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Router>
          <Layout>
            <Routes>
              <Route exact path="/" element={<RecipesList />} />
              <Route exact path="/logout" element={<Logout />} />
              <Route exact path="/user/login" element={<UserLogin />} />
              <Route exact path="/user/register" element={<UserRegister />} />
              <Route
                exact
                path="/user/dashboard"
                element={
                  <PrivateRoute
                    path="/user/dashboard"
                    element={UserDashboard}
                  />
                }
              />
              <Route
                exact
                path="/user/profile"
                element={
                  <PrivateRoute path="/user/profile" element={UserProfile} />
                }
              />

              <Route
                exact
                path="/user/my-recipes"
                element={
                  <PrivateRoute
                    path="/user/my-recipes"
                    element={MyRecipesList}
                  />
                }
              />
              <Route
                exact
                path="/user/my-recipes/create-new"
                element={
                  <PrivateRoute
                    path="/user/my-recipes/create-new"
                    element={CreateRecipe}
                  />
                }
              />
              <Route
                exact
                path="/user/my-recipes/:recipeId/update-recipe"
                element={
                  <PrivateRoute
                    path="/user/my-recipes/:recipeId/update-recipe"
                    element={UpdateRecipe}
                  />
                }
              />
              <Route
                exact
                path="/user/my-recipes/:recipeId/details"
                element={
                  <PrivateRoute
                    path="/user/my-recipes/:recipeId/details"
                    element={RecipeDetails}
                  />
                }
              />
              <Route
                exact
                path="/recipes"
                element={<PrivateRoute path="/recipes" element={RecipesList} />}
              />
              <Route
                exact
                path="/recipes/:recipeId/details"
                element={
                  <PrivateRoute
                    path="/recipes/:recipeId/details"
                    element={RecipeDetails}
                  />
                }
              />
              <Route
                exact
                path="/user/subscription"
                element={
                  <PrivateRoute
                    path="/user/subscription"
                    element={SubscriptionPage}
                  />
                }
              />

              <Route exact path="/admin/login" element={<AdminLogin />} />

              <Route
                exact
                path="/admin/dashboard"
                element={
                  <PrivateRoute
                    path="/admin/dashboard"
                    element={AdminDashboard}
                  />
                }
              />
              <Route
                exact
                path="/admin/users"
                element={
                  <PrivateRoute path="/admin/users" element={UsersList} />
                }
              />
              <Route
                exact
                path="/admin/recipes"
                element={
                  <PrivateRoute
                    path="/admin/recipes"
                    element={AdminRecipesList}
                  />
                }
              />
              <Route
                exact
                path="/admin/recipes/:recipeId/details"
                element={
                  <PrivateRoute
                    path="/admin/recipes/:recipeId/details"
                    element={RecipeDetails}
                  />
                }
              />
              <Route
                exact
                path="/admin/recipes/:recipeId/comments"
                element={
                  <PrivateRoute
                    path="/admin/recipes/:recipeId/comments"
                    element={AdminRecipeCommentsList}
                  />
                }
              />

              <Route path="*" element={<PageNotFound />} />
            </Routes>
          </Layout>
        </Router>
      </ThemeProvider>
    </AuthProvider>
  );
}

export default App;
