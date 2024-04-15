import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Box, Button, Container, TextField, Typography } from "@mui/material";
import { useAuth } from "../../contexts/AuthContext";
import api from "../../api/api";

const StyledLink = ({ children, ...props }) => (
  <Link style={{ textDecoration: "none", color: "inherit" }} {...props}>
    {children}
  </Link>
);

const AdminLogin = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async () => {
    try {
      const response = await api.post("/admin/login", { email, password });
      const { token, adminId, ...rest } = response.data;

      if (token) {
        login(token, { adminId, ...rest });
        navigate("/admin/dashboard");
      } else {
        setError("Invalid credentials");
      }
    } catch (err) {
      setError("Invalid email or password");
    }
  };

  return (
    <Container
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
      }}
    >
      <Box
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          padding: "32px",
          backgroundColor: "#ffffff",
          borderRadius: "8px",
          boxShadow: "0 3px 10px rgba(0, 0, 0, 0.2)",
          width: "100%",
          maxWidth: "400px",
          gap: "16px",
          marginTop: 72,
        }}
      >
        <Typography variant="h5" component="h1" gutterBottom>
          Admin Login
        </Typography>
        {error && <Typography color="error">{error}</Typography>}
        <TextField
          label="Email"
          variant="outlined"
          size="small"
          fullWidth
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
        <TextField
          label="Password"
          type="password"
          variant="outlined"
          size="small"
          fullWidth
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          InputLabelProps={{ shrink: true }}
        />
        <Button
          variant="contained"
          color="primary"
          onClick={handleLogin}
          fullWidth
        >
          Login
        </Button>
        <Link to="/user/login">Login as user</Link>
      </Box>
    </Container>
  );
};

export default AdminLogin;
