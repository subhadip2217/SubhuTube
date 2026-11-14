import { Link, useLocation } from 'react-router-dom';
import {
  Home,
  Compass,
  PlaySquare,
  Clock,
  ThumbsUp,
  History,
  Film,
  Music,
  Gamepad2,
  Newspaper,
} from 'lucide-react';

const mainLinks = [
  { to: '/', icon: Home, label: 'Home' },
  { to: '/explore', icon: Compass, label: 'Explore' },
  { to: '/shorts', icon: PlaySquare, label: 'Shorts' },
  { to: '/subscriptions', icon: PlaySquare, label: 'Subscriptions' },
];

const libraryLinks = [
  { to: '/history', icon: History, label: 'History' },
  { to: '/liked', icon: ThumbsUp, label: 'Liked videos' },
  { to: '/watch-later', icon: Clock, label: 'Watch later' },
];

const exploreLinks = [
  { to: '/music', icon: Music, label: 'Music' },
  { to: '/movies', icon: Film, label: 'Movies' },
  { to: '/gaming', icon: Gamepad2, label: 'Gaming' },
  { to: '/news', icon: Newspaper, label: 'News' },
];

function NavItem({ to, icon: Icon, label, collapsed }) {
  const location = useLocation();
  const active = location.pathname === to;

  return (
    <Link
      to={to}
      className={`flex items-center gap-6 px-3 py-2.5 rounded-xl transition ${
        active ? 'bg-tube-hover font-medium' : 'hover:bg-tube-hover'
      } ${collapsed ? 'justify-center' : ''}`}
    >
      <Icon size={22} strokeWidth={active ? 2.5 : 2} />
      {!collapsed && <span className="text-sm">{label}</span>}
    </Link>
  );
}

export default function Sidebar({ open, collapsed }) {
  return (
    <aside
      className={`fixed top-14 left-0 bottom-0 z-40 bg-tube-dark overflow-y-auto transition-all duration-200 ${
        open ? (collapsed ? 'w-[72px]' : 'w-60') : 'w-0 -translate-x-full'
      }`}
    >
      <nav className="py-3 px-3 space-y-1">
        {mainLinks.map((link) => (
          <NavItem key={link.to} {...link} collapsed={collapsed} />
        ))}

        {!collapsed && (
          <>
            <div className="border-t border-white/10 my-3" />
            <p className="px-3 py-1 text-sm font-medium text-tube-muted">Library</p>
            {libraryLinks.map((link) => (
              <NavItem key={link.to} {...link} collapsed={collapsed} />
            ))}

            <div className="border-t border-white/10 my-3" />
            <p className="px-3 py-1 text-sm font-medium text-tube-muted">Explore</p>
            {exploreLinks.map((link) => (
              <NavItem key={link.to} {...link} collapsed={collapsed} />
            ))}

            <div className="border-t border-white/10 my-3" />
            <div className="px-3 py-2 text-xs text-tube-muted leading-relaxed">
              <p className="font-medium text-tube-text mb-1">SubhuTube</p>
              <p>100% Ad-Free experience.</p>
              <p className="mt-2">No ads. No tracking. Just videos.</p>
            </div>
          </>
        )}
      </nav>
    </aside>
  );
}