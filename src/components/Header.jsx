import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Search, Menu, Mic, Bell, User } from 'lucide-react';

export default function Header({ onMenuClick }) {
  const [query, setQuery] = useState('');
  const navigate = useNavigate();

  const handleSearch = (e) => {
    e.preventDefault();
    if (query.trim()) {
      navigate(`/search?q=${encodeURIComponent(query.trim())}`);
    }
  };

  return (
    <header className="fixed top-0 left-0 right-0 z-50 h-14 bg-tube-dark border-b border-white/10 flex items-center px-4 gap-4">
      <button
        onClick={onMenuClick}
        className="p-2 rounded-full hover:bg-tube-hover transition"
        aria-label="Menu"
      >
        <Menu size={22} />
      </button>

      <Link to="/" className="flex items-center gap-1 shrink-0">
        <div className="w-8 h-8 bg-tube-red rounded-lg flex items-center justify-center">
          <svg viewBox="0 0 24 24" className="w-5 h-5 fill-white">
            <path d="M8 5v14l11-7z" />
          </svg>
        </div>
        <span className="text-xl font-semibold tracking-tight hidden sm:block">
          Subhu<span className="text-tube-red">Tube</span>
        </span>
        <span className="text-[10px] text-tube-muted ml-1 hidden md:inline">AD-FREE</span>
      </Link>

      <form onSubmit={handleSearch} className="flex-1 max-w-2xl mx-auto flex">
        <div className="flex flex-1 items-center bg-tube-dark border border-white/20 rounded-l-full overflow-hidden focus-within:border-blue-500">
          <input
            type="search"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search"
            className="flex-1 bg-transparent px-4 py-2 outline-none text-sm placeholder:text-tube-muted"
          />
        </div>
        <button
          type="submit"
          className="px-5 bg-tube-card border border-l-0 border-white/20 rounded-r-full hover:bg-tube-hover transition"
          aria-label="Search"
        >
          <Search size={20} />
        </button>
        <button
          type="button"
          className="ml-2 p-2.5 rounded-full bg-tube-card hover:bg-tube-hover transition hidden sm:flex"
          aria-label="Voice search"
        >
          <Mic size={20} />
        </button>
      </form>

      <div className="flex items-center gap-1 shrink-0">
        <button className="p-2 rounded-full hover:bg-tube-hover transition hidden sm:flex" aria-label="Notifications">
          <Bell size={22} />
        </button>
        <button className="p-1.5 rounded-full hover:bg-tube-hover transition">
          <div className="w-8 h-8 rounded-full bg-tube-red flex items-center justify-center text-sm font-medium">
            S
          </div>
        </button>
      </div>
    </header>
  );
}