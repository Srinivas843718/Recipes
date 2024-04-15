import {
  AppBar,
  Avatar,
  Button,
  Menu,
  MenuItem,
  Toolbar,
  Typography,
} from "@mui/material";
import React,{ useState } from "react";
import { Link } from "react-router-dom";
import styled from "styled-components";
import { useAuth } from "../../contexts/AuthContext";

const StyledLink = styled(Link)`
  text-decoration: none;
  color: white;
  margin-left: 20px;
`;

const Header = () => {
  const { isAdmin, isAuthenticated, isUser, user } = useAuth();
  const isLoggedIn = isAuthenticated();
  const [anchorEl, setAnchorEl] = useState(null);

  const handleMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  return (
    <AppBar position="sticky" style={{ top: 0 }}>
      <Toolbar>
        <StyledLink to="/">
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Recipe Management
          </Typography>
        </StyledLink>

        <div style={{ marginLeft: "auto" }}>
          {isAdmin() && isLoggedIn && (
            <>
              <StyledLink to="/admin/dashboard">
                <Button color="inherit">Dashboard</Button>
              </StyledLink>
            </>
          )}
          {!isAdmin() && (
            <>
              <StyledLink to="/recipes">
                <Button color="inherit">Recipes</Button>
              </StyledLink>
            </>
          )}
          {isUser() && isLoggedIn && (
            <>
              <StyledLink to="/user/my-recipes">
                <Button color="inherit">My Recipes</Button>
              </StyledLink>
              <StyledLink to="/user/dashboard">
                <Button color="inherit">Dashboard</Button>
              </StyledLink>
            </>
          )}

          {!isLoggedIn && (
            <>
              <StyledLink to="/user/login">
                <Button color="inherit">Login</Button>
              </StyledLink>
              <StyledLink to="/user/register">
                <Button color="inherit">Register</Button>
              </StyledLink>
            </>
          )}
          {isLoggedIn && (
            <>
              <Button onClick={handleMenuOpen}>
                <Avatar
                  style={{
                    backgroundColor: "white",
                    color: "black",
                    fontSize: "16px", // Set the font size
                    width: "32px", // Set the width
                    height: "32px", // Set the height
                  }}
                >
                  {user?.firstName?.charAt(0)}
                </Avatar>
              </Button>
              <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
              >
                {!isAdmin() ? (
                  <Link style={{ textDecoration: "none" }} to={"/user/profile"}>
                    <MenuItem
                      style={{ color: "black" }}
                      onClick={handleMenuClose}
                    >
                      {user?.firstName}
                    </MenuItem>
                  </Link>
                ) : (
                  <MenuItem onClick={handleMenuClose}>
                    {user?.firstName}
                  </MenuItem>
                )}
                <Link style={{ textDecoration: "none" }} to="/logout">
                  <MenuItem style={{ color: "black" }}>Logout</MenuItem>
                </Link>
              </Menu>
            </>
          )}
        </div>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
