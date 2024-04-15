import React from "react";

const SubscriptionDetails = ({ subscription }) => {
  return (
    <div>
      <h2>Your Subscription Details</h2>
      {subscription ? (
        <p>You have an active subscription. Enjoy premium content!</p>
      ) : (
        <p>Please make a payment to subscribe!</p>
      )}
    </div>
  );
};

export default SubscriptionDetails;
