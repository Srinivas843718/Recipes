import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

const adminUrls = [
  "/admin/dashboard",
  "/admin/users",
  "/admin/users/:id",
  "/admin/recipes",
  "/admin/recipes/:recipeId/details",
  "/admin/recipes/:recipeId/comments",
  // Add other admin URLs as needed
];

const publicUrls = ["/recipes", "/recipes/:recipeId/details"];

const userUrls = [
  "/user/dashboard",
  "/user/profile",
  "/user/courses",
  "/user/courses/:courseId",
  "/user/my-recipes",
  "/user/my-recipes/create-new",
  "/user/my-recipes/:recipeId/update-recipe",
  "/user/my-recipes/:recipeId/details",
  "/user/subscription",
  "/user/profile",
];

function PrivateRoute({ path, element: Component }) {
  const { isAuthenticated, isAdmin, isUser } = useAuth();

  let hasAccess = false;
  if (publicUrls.includes(path)) {
    hasAccess = true;
  } else if (adminUrls.includes(path)) {
    hasAccess = isAuthenticated() && isAdmin();
  } else if (userUrls.includes(path)) {
    hasAccess = isAuthenticated() && isUser();
  }
  return <>{hasAccess ? <Component /> : <Navigate to="/access-denied" />}</>;
}

export default PrivateRoute;
