// // src/components/MainLayout.js
// import React, { useContext, useEffect, useState } from 'react';
// import Header from './Header';
// import Footer from './Footer';
// import AdminDashboard from './AdminDashboard';
// import UserDashboard from '../pages/Home'; // reuse Home for user
// import { AuthContext } from '../context/AuthContext';

// const MainLayout = () => {
//   const { user } = useContext(AuthContext);
//   const [middleContent, setMiddleContent] = useState(null);

//   useEffect(() => {
//     const roles = user?.roles || [];

//     if (roles.includes('ROLE_ADMIN')) {
//       setMiddleContent(<AdminDashboard />);
//     } else if (roles.includes('ROLE_USER')) {
//       setMiddleContent(<UserDashboard />);
//     } else {
//       setMiddleContent(<UserDashboard />); // fallback to Home for guests
//     }
//   }, [user]);

//   return (
//     <>
//       <Header />
//       <main>{middleContent}</main>
//       <Footer />
//     </>
//   );
// };

// export default MainLayout;


import React, { useContext, useEffect, useState } from 'react';
import Header from './Header';
import Footer from './Footer';
import AdminDashboard from './AdminDashboard';
import UserDashboard from '../pages/Home'; // reuse Home for user
import { AuthContext } from '../context/AuthContext';
import ReceptionistDashboard from './ReceptionistDashboard';

const MainLayout = ({ content }) => {
  const { user } = useContext(AuthContext);
  const [middleContent, setMiddleContent] = useState(null);

  useEffect(() => {
    if (content) {
      setMiddleContent(content); // ✅ Use passed content if available
    } else {
      const roles = user?.roles || [];
      if (roles.includes('ROLE_ADMIN')) {
        setMiddleContent(<AdminDashboard />);
      }else if(roles.includes('ROLE_RECEPTIONIST')){
        setMiddleContent(<ReceptionistDashboard/>);
      } else {
        setMiddleContent(<UserDashboard />);
      }
    }
  }, [user, content]);

  return (
    <>
      <Header />
      <main>{middleContent}</main>
      <Footer />
    </>
  );
};

export default MainLayout;