import { Box, Divider, Paper, Typography } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";
import { useAuth } from "../../contexts/AuthContext";
import CustomSnackbar from "../home/CustomSnackbar";
import PaymentForm from "./PaymentForm";
import SubscriptionDetails from "./SubscriptionDetails";

export const PREMIUM_PRICE = 30;

const SubscriptionPage = () => {
  const totalPrice = PREMIUM_PRICE;
  const { user, isAuthenticated, isUser } = useAuth();
  const [snackbar, setSnackbar] = useState({
    message: "",
    alertType: "",
  });
  const [openSnackbar, setOpenSnackbar] = useState(false);
  const navigate = useNavigate();
  const [subscription, setSubscription] = useState(null);

  useEffect(() => {
    let timeout;

    const fetchData = async () => {
      if (!isAuthenticated() || (isAuthenticated() && !isUser())) {
        setSnackbar({
          message: "You need to login first.",
          alertType: "info",
        });
        setOpenSnackbar(true);

        timeout = setTimeout(() => {
          navigate("/user/login");
        }, 2000);
      } else {
        try {
          const res = await api.get(`/subscriptions/user/${user?.userId}`);
          setSubscription(res.data);
        } catch (error) {
          console.error("Error fetching subscription details:", error);
        }
      }
    };

    fetchData();

    return () => clearTimeout(timeout); // Cleanup the timeout on component unmount
  }, [isAuthenticated, isUser, navigate, user?.userId]);

  const handlePlaceOrder = async ({
    cardNumber,
    cardName,
    expiryMonth,
    expiryYear,
    cvv,
  }) => {
    const PAID = "Paid";
    const CARD = "Card";
    const subscriptionData = {
      userId: user?.userId,
      username: user?.firstName + " " + user?.lastName,
      amount: totalPrice,
      status: "Success",
    };

    const paymentDetails = {
      paymentType: CARD,
      totalAmount: totalPrice,
      cardNumber,
      cardName,
      expiryMonth,
      expiryYear,
      cvv,
      status: PAID,
    };

    const payload = { ...subscriptionData, paymentDetails };

    try {
      await api.post(`/subscriptions`, payload);

      setSnackbar({
        message: "Subscribed successfully",
        alertType: "success",
      });
      setOpenSnackbar(true);
      const res = await api.get(`/subscriptions/user/${user?.userId}`);
      setSubscription(res.data);
      setTimeout(() => {
        navigate("/recipes");
      }, 4000);
      return "success";
    } catch (err) {
      setSnackbar({ message: err?.message, alertType: "error" });
      setOpenSnackbar(true);
      console.error(err);
      return "fail";
    }
  };

  return (
    <div>
      <Box sx={{ display: "flex", gap: "16px", width: "100%" }}>
        <Paper
          elevation={3}
          sx={{
            padding: "16px",
            flex: 1,
            overflowY: "auto",
            maxHeight: "calc(100vh - 64px)",
          }}
        >
          <Typography variant="h5">Subscription Page</Typography>
          <Divider sx={{ margin: "8px 0" }} />
          <SubscriptionDetails subscription={subscription} />
        </Paper>

        {!subscription && (
          <PaymentForm
            totalPrice={totalPrice}
            handlePayment={handlePlaceOrder}
          />
        )}
      </Box>
      <CustomSnackbar
        open={openSnackbar}
        handleClose={() => {
          setSnackbar({
            message: "",
            alertType: "",
          });
          setOpenSnackbar(false);
        }}
        {...snackbar}
      />
    </div>
  );
};

export default SubscriptionPage;
