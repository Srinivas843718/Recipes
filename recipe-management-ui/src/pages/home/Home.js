import React from "react";
import { Container, Typography, Button, Box, Grid } from "@mui/material";
import { Link } from "react-router-dom";

const Home = () => {
  return (
    <Container>
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          height: "100vh",
        }}
      >
        <Typography variant="h3" gutterBottom sx={{ marginBottom: 2 }}>
          Welcome to Recipe Database Management System
        </Typography>
        <Typography variant="body1" gutterBottom sx={{ marginBottom: 4 }}>
          Taste the success once, tongue wants more.
        </Typography>
        <Grid container spacing={2}>
          <Grid item xs={12} md={6}>
            <Box
              sx={{
                backgroundColor: "#f5f5f5",
                padding: "2rem",
                borderRadius: "10px",
              }}
            >
              <Typography variant="h5" gutterBottom>
                A variety of Recipies
              </Typography>
              <Typography variant="body1" gutterBottom>
                Explore a wide range of Recipies in the database with all required ingredients and cooking instructions.
              </Typography>
            </Box>
          </Grid>
          <Grid item xs={12} md={6}>
            <Box
              sx={{
                backgroundColor: "#f5f5f5",
                padding: "2rem",
                borderRadius: "10px",
              }}
            >
              <Typography variant="h5" gutterBottom>
                {/* Empower Educators */}
              </Typography>
              <Typography variant="body1" gutterBottom>
                {/* Instructors can efficiently manage classes, assignments, and
                student progress. Streamline your teaching experience and focus
                on empowering your students. */}
              </Typography>
              {/* <Button
                variant="contained"
                component={Link}
                to="/instructors"
                sx={{ marginTop: 2 }}
              >
                Explore Instructors
              </Button> */}
            </Box>
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
};

export default Home;
