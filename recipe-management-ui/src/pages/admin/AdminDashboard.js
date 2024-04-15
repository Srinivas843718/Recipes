import React from "react";
import styled from "styled-components";
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Button,
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const StyledBox = styled(Box)`
  padding: 32px;
`;

const StyledCard = styled(Card)`
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 10px;
  transition: transform 0.3s ease-in-out;

  &:hover {
    transform: scale(1.05);
  }
`;

const StyledCardContent = styled(CardContent)`
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  justify-content: center;
`;

const StyledTypography = styled(Typography)`
  text-align: center;
`;

const StyledButton = styled(Button)`
  margin-top: 16px;
`;

const AdminDashboard = () => {
  const navigate = useNavigate();

  const cardData = [
    {
      title: "Manage Recipes",
      description: "Manage All Recipes",
      link: "/admin/recipes",
    },
    {
      title: "Manage Users",
      description: "Manage User Profiles",
      link: "/admin/users",
    },
  ];

  return (
    <StyledBox>
      <Grid container spacing={3}>
        {cardData.map((data, index) => (
          <Grid key={index} item xs={12} md={6} lg={4}>
            <StyledCard onClick={() => navigate(data.link)}>
              <StyledCardContent>
                <StyledTypography gutterBottom variant="h5" component="h2">
                  {data.title}
                </StyledTypography>
                <StyledTypography variant="body2" component="p">
                  {data.description}
                </StyledTypography>
              </StyledCardContent>
              <StyledButton size="small" color="primary">
                View {data.title}
              </StyledButton>
            </StyledCard>
          </Grid>
        ))}
      </Grid>
    </StyledBox>
  );
};

export default AdminDashboard;
