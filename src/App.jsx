import { useState } from 'react';
import { Routes, Route } from 'react-router-dom';
import Header from './components/Header';
import Sidebar from './components/Sidebar';
import Home from './pages/Home';
import Watch from './pages/Watch';
import Search from './pages/Search';
import Category from './pages/Category';

export default function App() {
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [collapsed, setCollapsed] = useState(false);

  const toggleSidebar = () => {
    if (window.innerWidth < 768) {
      setSidebarOpen((prev) => !prev);
    } else {
      setCollapsed((prev) => !prev);
    }
  };

  return (
    <div className="min-h-screen bg-tube-dark">
      <Header onMenuClick={toggleSidebar} />
      <Sidebar open={sidebarOpen} collapsed={collapsed} />

      <main
        className={`pt-14 transition-all duration-200 ${
          sidebarOpen
            ? collapsed
              ? 'md:ml-[72px]'
              : 'md:ml-60'
            : 'ml-0'
        }`}
      >
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/watch/:id" element={<Watch />} />
          <Route path="/search" element={<Search />} />
          <Route path="/:name" element={<Category />} />
        </Routes>
      </main>
    </div>
  );
}